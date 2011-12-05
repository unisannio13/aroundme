package it.unisannio.aroundme.client.model;

import java.util.Collection;
import java.util.HashSet;

import org.w3c.dom.Node;

import it.unisannio.aroundme.model.DataListener;
import it.unisannio.aroundme.model.Interest;
import it.unisannio.aroundme.model.InterestQuery;
import it.unisannio.aroundme.model.Serializer;
import it.unisannio.aroundme.model.SerializerUtils;

/**
 * 
 * @author Michele Piccirillo <michele.piccirillo@gmail.com>
 *
 */
public class InterestQueryImpl extends InterestQuery {

	@Override
	public void perform(final DataListener<Collection<Interest>> l) {
		XmlClient service = null; // TODO unimplemented
		service.post("/query/interest", SERIALIZER.toXML(this), new DataListener<Node>() {

			@Override
			public void onData(Node object) {
				Serializer<? extends Collection<Interest>> s = SerializerUtils.getCollectionSerializer(Interest.class);
				l.onData(s.fromXML(object));
				
			}

			@Override
			public void onError(Exception e) {
				l.onError(e);
			}
			
		});
	}

}