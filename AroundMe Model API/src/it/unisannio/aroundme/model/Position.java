package it.unisannio.aroundme.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Modello per una posizione geografica.
 * 
 * @author Michele Piccirillo <michele.piccirillo@gmail.com>
 */
public abstract class Position implements Model {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Serializzatore di questo modello.
	 * 
	 * Il formato utilizzato nella codifica &egrave;:
	 * <pre><code>
	 * &lt;position lat="0.0" lon="0.0" /&gt;
	 * </code></pre>
	 * 
	 * @see Serializer
	 */
	public static final Serializer<Position> SERIALIZER = new Serializer<Position>() {

		@Override
		public Position fromXML(Element node) {
			validateTagName(node, "position");
			
			double lat = Double.parseDouble(getRequiredAttribute(node, "lat"));
			double lon = Double.parseDouble(getRequiredAttribute(node, "lon"));
			
			Position obj = ModelFactory.getInstance().createPosition(lat, lon);
			
			return obj;
		}

		@Override
		public Element toXML(Position obj) {
			Document document = getDocumentBuilder().newDocument();
			
			Element e = document.createElement("position");
			e.setAttribute("lat", String.valueOf(obj.getLatitude()));
			e.setAttribute("lon", String.valueOf(obj.getLongitude()));
			
			return e;
		}
		
	};
	
	/**
	 * Restituisce la distanza in metri tra la Position corrente
	 * e una una  Position data.
	 * 
	 * @param p La Position rispetto a cui si vuole calcolare la distanza
	 * @return La distanza in metri tra la Position corrente e la Position p
	 * 
	 * @author Danilo Iannelli <daniloiannelli6@gmail.com>
	 */
	public int getDistance(Position p){
		double lat1 = Math.toRadians(getLatitude());
		double lat2 = Math.toRadians(p.getLatitude());
		double lon1 = Math.toRadians(getLongitude());
		double lon2 = Math.toRadians(p.getLongitude());
		double dist =  Math.cos(lon1 -lon2) * Math.cos(lat1) * Math.cos(lat2) +  Math.sin(lat1) * Math.sin(lat2);
		dist = Math.acos(dist) * 6378;
		return (int) Math.round(dist * 1000);
	}

	/**
	 * Restituisce la misura della latitudine.
	 * 
	 * @return la latitudine, espressa in gradi decimali
	 */
	public abstract double getLatitude();

	/**
	 * Restituisce la misura della longitudine.
	 * 
	 * @return la longitudine, espressa in gradi decimali
	 */
	public abstract double getLongitude();
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Position))
			return false;
		
		Position other = (Position) obj;
		return other.getLatitude() == getLatitude() && other.getLongitude() == getLongitude();
	}
	
	@Override
	public String toString() {
		return "(" + getLatitude() + ", " + getLongitude() + ")";
	}
}
