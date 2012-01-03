package it.unisannio.aroundme.server.servlet;

import it.unisannio.aroundme.model.Position;
import it.unisannio.aroundme.server.UserImpl;
import it.unisannio.aroundme.server.task.PositionQueryTask;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * 
 * @author Danilo Iannelli <daniloiannelli6@gmail.com>
 */
public class PositionServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			String userId = req.getRequestURI().split("user/(.*?)/position")[0]; //FIXME Usare un altro modo per ottenere l'userId
			Objectify ofy = ObjectifyService.begin();
			UserImpl user = ofy.get(UserImpl.class, Long.parseLong(userId));
			Position position = Position.SERIALIZER.read(req.getInputStream());
			user.setPosition(position);
			ofy.put(user);
			Queue queue = QueueFactory.getDefaultQueue();
			TaskOptions url = TaskOptions.Builder.withUrl(PositionQueryTask.URI)
					.param("userId", userId)
					.method(Method.POST);
			queue.add(url);

		}catch (Exception e){
			e.printStackTrace();
			resp.sendError(500);
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		String userId = req.getRequestURI().split("user/(.*?)/position")[0];
		Objectify ofy = ObjectifyService.begin();
		UserImpl user = ofy.get(UserImpl.class, Long.parseLong(userId));
		if(user != null){
			try {
				resp.setContentType("text/xml");
				Position.SERIALIZER.write(user.getPosition(), resp.getOutputStream());
			} catch (NullPointerException e) {
				e.printStackTrace();
				resp.sendError(404);
			} 
			catch (Exception e) {
				e.printStackTrace();
				resp.sendError(500);
			}
		}
	}
}
