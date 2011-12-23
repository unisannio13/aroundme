package it.unisannio.aroundme.server;

import it.unisannio.aroundme.model.DataListener;
import it.unisannio.aroundme.model.SerializerUtils;
import it.unisannio.aroundme.model.User;
import it.unisannio.aroundme.model.UserQuery;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;



/**
 * @author Danilo Iannelli <daniloiannelli6@gmail.com>
 *
 */
public class UserQueryServlet extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, final HttpServletResponse resp)	throws ServletException, IOException {
		try {
			UserQuery query = UserQuery.SERIALIZER.fromXML(SerializerUtils.getDocumentBuilder().parse(req.getInputStream()));
			query.perform(new DataListener<Collection<? extends User>>() {
				@Override
				public void onData(Collection<? extends User> object) {
					resp.setContentType("text/xml");
					Node xml = SerializerUtils.getCollectionSerializer(User.class).toXML(object);
					try {
						SerializerUtils.writeXML(xml, resp.getOutputStream());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onError(Exception e) {
					try {
						resp.sendError(500);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

