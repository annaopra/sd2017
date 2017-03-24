package fsega.distributedsystems.server.helpers;

public enum HttpContentType {
	Text ("text/html"),
	Json ("application/json");
	
	private final String contentType;
	
	private HttpContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getContentType() {
		return contentType;
	}
}
