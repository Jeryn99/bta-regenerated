package mc.jeryn.dev.regen.bta.skin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mc.jeryn.dev.regen.bta.Regeneration;

import javax.imageio.ImageIO;

public class SkinDownloader {

	private static final String SKINS_API_URL = "https://api.jeryn.dev/mc/skins"; // API endpoint

	// This map will store skins with their type (slim or default) and their metadata.
	public static Map<String, List<SkinData>> skinMap = new HashMap<>();

	public static SkinData getRandomSkin() {
		if (skinMap.isEmpty()) {
			Regeneration.LOGGER.error("Skin map is empty. Please collect skin data first.");
			return null;
		}

		// Randomly choose between "slim" and "default"
		String[] skinTypes = skinMap.keySet().toArray(new String[0]);
		String randomSkinType = skinTypes[Regeneration.MASTER_RANDOM.nextInt(skinTypes.length)];

		// Get the list of skins for the randomly chosen skin type
		List<SkinData> skins = skinMap.get(randomSkinType);
		if (skins == null || skins.isEmpty()) {
			Regeneration.LOGGER.error("No skins found for the chosen type: {}", randomSkinType);
			return null;
		}

		// Randomly select a skin from the list
		SkinData randomSkin = skins.get(Regeneration.MASTER_RANDOM.nextInt(skins.size()));
		Regeneration.LOGGER.error("Randomly selected skin: {}", randomSkin);
		return randomSkin;
	}

	public static void collectSkinData() {
		try {
			// Fetch the skin data from the API
			List<SkinData> skinDataList = fetchSkinData(SKINS_API_URL);

			// Process each skin without downloading
			for (SkinData skinData : skinDataList) {
				// Check the type (slim or default) based on the image pixels (metadata only)
				boolean isSlim = checkIfSlim(skinData.link);
				skinData.setSlim(isSlim);
				// Add to the map (key: skin type, value: list of SkinData)
				String skinType = isSlim ? "slim" : "default";
				skinMap.computeIfAbsent(skinType, k -> new ArrayList<>()).add(skinData);
			}

			Regeneration.LOGGER.debug("Collected skin data and classified by type.");
		} catch (IOException e) {
			Regeneration.LOGGER.error("Error fetching skin data from API.");
			e.printStackTrace();
		}
	}

	private static List<SkinData> fetchSkinData(String apiUrl) throws IOException {
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			return parseSkinData(response.toString());
		} else {
			throw new IOException("Failed to fetch skin data from API: " + apiUrl);
		}
	}

	private static List<SkinData> parseSkinData(String jsonResponse) {
		List<SkinData> skinDataList = new ArrayList<>();

		JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();
		for (JsonElement element : jsonArray) {
			JsonObject skinObject = element.getAsJsonObject();
			String name = skinObject.get("name").getAsString();
			String link = skinObject.get("link").getAsString();
			skinDataList.add(new SkinData(name, link));
		}

		return skinDataList;
	}

	private static boolean checkIfSlim(String skinUrl) throws IOException {
		// Download the skin image using the provided URL
		BufferedImage skinImage = downloadSkin(skinUrl);

		// Now we perform the pixel check as discussed earlier
		int pixelWidth = skinImage.getWidth();
		int pixelHeight = skinImage.getHeight();

		// Check if the skin image has standard Minecraft skin dimensions (64x64)
		if (pixelWidth != 64 || pixelHeight != 64) {
			System.out.println("Invalid skin dimensions: " + pixelWidth + "x" + pixelHeight);
			return false;
		}

		// In the slim model, the arm is 3 pixels wide instead of 4 (default)
		// Check the right arm (x = 46 for slim, x = 44 for default)
		int slimArmStartX = 46;
		int armYStart = 52;
		int armHeight = 12;

		// Check the transparency of the pixels in the slim arm region (x = 46 to 48)
		// If the pixels are transparent, it's the slim model
		for (int x = slimArmStartX; x < slimArmStartX + 3; x++) {
			for (int y = armYStart; y < armYStart + armHeight; y++) {
				int pixelAlpha = (skinImage.getRGB(x, y) >> 24) & 0xff; // Get the alpha value of the pixel
				if (pixelAlpha != 0) {
					// If any pixel in this region is not fully transparent, it's not a slim skin
					return false;
				}
			}
		}

		// If all relevant pixels are transparent, it's a slim skin
		return true;
	}

	private static BufferedImage downloadSkin(String skinUrl) throws IOException {
		// Create a URL object from the provided skin URL string
		URL url = new URL(skinUrl);

		// Open a connection to the URL
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		// Check if the response code is HTTP OK (200)
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// Read the image directly from the input stream
			return ImageIO.read(connection.getInputStream());
		} else {
			// If the response code is not OK, throw an exception with the response message
			throw new IOException("Failed to download skin. HTTP Response Code: " + connection.getResponseCode());
		}
	}


	// SkinData class to hold skin information
	public static class SkinData {
		String name;
		String link;
		boolean isSlim;

		SkinData(String name, String link) {
			this.name = name;
			this.link = link;
		}

		public String getLink() {
			return link;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "SkinData{" +
				"name='" + name + '\'' +
				", link='" + link + '\'' +
				'}';
		}

		public void setSlim(boolean slim) {
			isSlim = slim;
		}

		public boolean isSlim() {
			return isSlim;
		}
	}

}
