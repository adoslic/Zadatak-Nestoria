import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
	
	public static void main (String[] args) {
		
		HttpClient client = HttpClient.newHttpClient();
		/*HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.nestoria.de/api?encoding=json&pretty=1&action=search_listings&country=de&place_name=berlin")).build();*/
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.nestoria.de/api?encoding=json&pretty=1&action=search_listings&country=de&place_name="+args[0])).build();
		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)
			.thenApply(Main::parse)
			.join();
	}
	
	public static String parse(String responseBody) {
		
		JSONObject allResponse = new JSONObject(responseBody);
		JSONObject response = allResponse.getJSONObject("response");
		JSONArray listings = response.getJSONArray("listings");
		if(listings.length() != 0) {
			
			System.out.println("Type \t Size \t Price");
			
			for( int i=0; i<listings.length(); i++) {
				JSONObject listing = listings.getJSONObject(i);
				
				String property;
				if (listing.has("property_type")) {
					property = listing.getString("property_type");
				}
				else {
					property = "-";
				}
				
				int size;
				if (listing.has("size")) {
					size = listing.getInt("size");
				}
				else {
					size = 0;
				}
				
				String price;
				if (listing.has("price_formatted")) {
					price = listing.getString("price_formatted");
				}
				else {
					price = "-";
				}
				System.out.println(property +"\t"+size + "\t" + price);
			}
		}
		else
			System.out.println("No match");
		
		return null;
	}
}
