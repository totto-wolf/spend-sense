package com.spendsense.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private WebView webView;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int REQUEST_SMS_PERMISSION = 1001;
    private static final int REQUEST_CONTACTS_PERMISSION = 1002;
    
    private String pendingJsCallback = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        
        // Setup WebView
        webView = new WebView(this);
        setContentView(webView);
        
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        
        // Add JavaScript interface
        webView.addJavascriptInterface(new AndroidBridge(), "Android");
        
        // Load the HTML file from assets
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/index.html");
    }

    public class AndroidBridge {
        
        @JavascriptInterface
        public void signInWithGoogle() {
            runOnUiThread(() -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });
        }
        
        @JavascriptInterface
        public boolean requestSMSPermission() {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                
                runOnUiThread(() -> {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS},
                            REQUEST_SMS_PERMISSION);
                });
                return false;
            }
            return true;
        }
        
        @JavascriptInterface
        public boolean requestContactsPermission() {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                
                runOnUiThread(() -> {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            REQUEST_CONTACTS_PERMISSION);
                });
                return false;
            }
            return true;
        }
        
        @JavascriptInterface
        public int getSMSCount() {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                return 0;
            }
            
            Cursor cursor = getContentResolver().query(
                    Uri.parse("content://sms/inbox"),
                    null, null, null, null);
            
            int count = cursor != null ? cursor.getCount() : 0;
            if (cursor != null) cursor.close();
            return count;
        }
        
        @JavascriptInterface
        public String readSMSBatch(int offset, int limit) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                return "[]";
            }
            
            try {
                JSONArray messages = new JSONArray();
                
                Cursor cursor = getContentResolver().query(
                        Uri.parse("content://sms/inbox"),
                        new String[]{"body", "date", "address"},
                        null, null, "date DESC LIMIT " + limit + " OFFSET " + offset);
                
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                        
                        // Filter for potential bank messages
                        if (body != null && (
                                body.toLowerCase().contains("debited") ||
                                body.toLowerCase().contains("credited") ||
                                body.toLowerCase().contains("spent") ||
                                body.toLowerCase().contains("inr") ||
                                body.toLowerCase().contains("rs.") ||
                                body.toLowerCase().contains("paid"))) {
                            messages.put(body);
                        }
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                
                return messages.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        }
        
        @JavascriptInterface
        public String readAllSMS() {
            return readSMSBatch(0, 1000); // Read up to 1000 messages
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            
            // Create JSON result
            JSONObject result = new JSONObject();
            result.put("email", account.getEmail());
            result.put("name", account.getDisplayName());
            result.put("picture", account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "");
            
            // Call JavaScript callback
            String js = "window.handleGoogleSignIn(" + result.toString() + ");";
            webView.evaluateJavascript(js, null);
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
            webView.evaluateJavascript("window.handleGoogleSignIn(null);", null);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        
        if (requestCode == REQUEST_SMS_PERMISSION) {
            webView.evaluateJavascript("window.handleSMSPermission(" + granted + ");", null);
        } else if (requestCode == REQUEST_CONTACTS_PERMISSION) {
            webView.evaluateJavascript("window.handleContactsPermission(" + granted + ");", null);
        }
    }
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
