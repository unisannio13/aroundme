package it.unisannio.aroundme.activities;

import it.unisannio.aroundme.R;
import it.unisannio.aroundme.client.Application;
import it.unisannio.aroundme.client.Identity;
import it.unisannio.aroundme.client.Setup;
import it.unisannio.aroundme.client.async.AsyncQueue;
import it.unisannio.aroundme.client.async.FutureListener;
import it.unisannio.aroundme.model.User;
import it.unisannio.aroundme.services.PositionTrackingService;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * 
 * @author Michele Piccirillo <michele.piccirillo@gmail.com>
 *
 */ 
public class LoginActivity extends FragmentActivity implements FutureListener<Identity>{
	
	private Facebook facebook = new Facebook(Setup.FACEBOOK_APP_ID);
	String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    private AsyncQueue async;
	private TextView txtLoading;
	/**
	 * Lo scopo di questa activity � quello di ottenere un accesso a facebook ed ottenere tutte le informazioni che occorrono
	 *  
	 *  La prima cosa da fare � ottenere le seguenti cose:
	 *  
	 *  - Oggetto Identity contenente tutte le informazioni dell' utente che sta utilizzando
	 *  
	 * */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.async = new AsyncQueue();
		
		setContentView(R.layout.login);
		//Button btnFacebookConnect = (Button) findViewById(R.id.btnFacebookConnect);
		txtLoading=(TextView) findViewById(R.id.txtLoginWait);
		mPrefs = getPreferences(MODE_PRIVATE);
        final String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        long id = mPrefs.getLong("userId", 0);
        
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        if(facebook.isSessionValid()) {
        	//service.asyncDo(Identity.login(id, access_token), this);
        }
		/*btnFacebookConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {*/
				facebook.authorize(LoginActivity.this, new String[] { "offline_access", "user_likes" }, new DialogListener() {

					@Override
					public void onComplete(Bundle values) {
						//Toast.makeText(getApplicationContext(), "Accesso Effettuato", Toast.LENGTH_SHORT).show();
						LoginActivity.this.txtLoading.setText("Caricamento informazioni");
	                    async.exec(Identity.create(facebook), new FutureListener<User>() {

							@Override
							public void onSuccess(User object) {
								SharedPreferences.Editor editor = mPrefs.edit();
			                    editor.putString("access_token", facebook.getAccessToken());
			                    editor.putLong("access_expires", facebook.getAccessExpires());
			                    editor.putLong("userId", object.getId());
			                    editor.commit();
			                    
			                    ((Application) getApplication()).addToCache(object);
			                    Identity.set(object, facebook.getAccessToken());
								LoginActivity.this.txtLoading.setText("Benvenuto! "+object.getName());
			                    startService(new Intent(LoginActivity.this, PositionTrackingService.class));
			                    startActivity(new Intent(LoginActivity.this, ListViewActivity.class));
			                	finish();
							}

							@Override
							public void onError(Throwable e) {
								Toast.makeText(LoginActivity.this, "Errore", Toast.LENGTH_LONG).show();
								e.printStackTrace();
								
							}
	                    	
	                    });
	                    /* TODO
	                     * Fai una richiesta al server. Se l'utente c'è, ridireziona alla ListView.
	                     * Altrimenti alla pagina di registrazione.
	                     */
						//startActivity(new Intent(LoginActivity.this, ListViewActivity.class));
						//finish();
					}

					@Override
					public void onFacebookError(FacebookError e) {
						Toast.makeText(getApplicationContext(), "Si � verificato un errore durante l'autorizzazione: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onError(DialogError e) {
						Toast.makeText(getApplicationContext(), "Errore di connessione", Toast.LENGTH_LONG).show();
						
					}

					@Override
					public void onCancel() {
						Toast.makeText(getApplicationContext(), "Autenticazione Annullata", Toast.LENGTH_LONG).show();
						
					}
					
				});
			}
			
		/*});
	}*/
	
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }


	@Override
	public void onSuccess(Identity object) {
		startActivity(new Intent(LoginActivity.this, ListViewActivity.class));
    	finish();
	}


	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		async.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		async.resume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		async.shutdown();
	}
    
}