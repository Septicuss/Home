package oxygen.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class DataUtilities {

	// Variables

	public final static String DELIMETER = "#";

	// Locations

	public static String serializeLocation(final Location location) {
		if (location == null) {
			return null;
		}

		String world = location.getWorld().getName();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		float yaw = location.getYaw();
		float pitch = location.getPitch();

		StringBuilder sb = new StringBuilder();
		sb.append(world + DELIMETER);
		sb.append(x + DELIMETER);
		sb.append(y + DELIMETER);
		sb.append(z + DELIMETER);
		sb.append(yaw + DELIMETER);
		sb.append(pitch);

		return sb.toString();
	}

	public static Location deserializeLocation(final String locationString) {
		if (locationString == null) {
			return null;
		}

		String finalDivider = DELIMETER;

		if (!locationString.contains(DELIMETER))
			finalDivider = ":";

		final String[] data = locationString.split(finalDivider);

		World world = Bukkit.getWorld(data[0]);
		double x = Double.valueOf(data[1]);
		double y = Double.valueOf(data[2]);
		double z = Double.valueOf(data[3]);

		if (data.length > 4) {
			float yaw = Float.valueOf(data[4]);
			float pitch = Float.valueOf(data[5]);
			return new Location(world, x, y, z, yaw, pitch);
		}

		return new Location(world, x, y, z);
	}

	// Numbers

	public static int getRandomNumber(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public static String getFormattedTime(long systemTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return formatter.format(new Date(systemTime));
	}

	// Lists

	public static <T> String toString(List<T> list) {
		StringBuilder sBuilder = new StringBuilder();

		for (Object object : list) {
			sBuilder.append(object.toString() + " ");
		}

		return sBuilder.toString().trim();
	}

	public static List<String> toList(String dataString) {
		return Arrays.asList(dataString.split(" "));
	}

	// Base64 Serialization

	public static String toBase64(Object object) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);

			objectOutput.writeObject(object);
			objectOutput.flush();

			byte[] bytes = outputStream.toByteArray();

			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to convert to base64", exception);
		}
	}

	public static Object fromBase64(String data) {
		try {
			byte[] bytes = Base64.getDecoder().decode(data);

			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			ObjectInputStream objectInput = new ObjectInputStream(inputStream);

			return objectInput.readObject();
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to load from base64", exception);
		}
	}

	public static String toBase64Bukkit(Object object) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream objectOutput = new BukkitObjectOutputStream(outputStream);

			objectOutput.writeObject(object);
			objectOutput.flush();

			byte[] bytes = outputStream.toByteArray();

			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to convert to base64", exception);
		}
	}

	public static Object fromBase64Bukkit(String data) {
		try {
			byte[] bytes = Base64.getDecoder().decode(data);

			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			BukkitObjectInputStream objectInput = new BukkitObjectInputStream(inputStream);

			return objectInput.readObject();
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to load from base64", exception);
		}
	}
}
