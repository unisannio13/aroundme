package it.unisannio.aroundme.overlay;

import java.text.MessageFormat;

import it.unisannio.aroundme.client.Identity;
import it.unisannio.aroundme.location.PositionUtils;
import it.unisannio.aroundme.model.User;

import com.google.android.maps.OverlayItem;

/**
 * 
 * @author Michele Piccirillo <michele.piccirillo@gmail.com>
 *
 */
public class UserOverlayItem extends OverlayItem {
	private final User user;
	
	public UserOverlayItem(User user) {
		// FIXME Externalize strings
		super(
				PositionUtils.toGeoPoint(user.getPosition()), 
				user.getName(), 
				(user.equals(Identity.get()) 
						? "Sei tu" 
						: MessageFormat.format("Compatibile al {0,number,percent}", Identity.get().getCompatibilityRank(user))
				)
				
		);
		
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	

}
