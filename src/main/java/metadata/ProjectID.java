package metadata;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

import org.json.JSONObject;

public final class ProjectID {
    public static String generate() {
        return UUID.randomUUID().toString();
    }

    public static String ensureExists(JSONObject json) {
        if (!json.has("projectId")) {
            String id = generate();
            json.put("projectId", id);
            return id;
        }
        return json.getString("projectId");
    }

    public static String ensureExists(File file) throws IOException {
        String text = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(text);
        String id = ensureExists(json);
        if (!text.contains("\"projectId\"")) {
            Files.writeString(file.toPath(), json.toString(4), StandardCharsets.UTF_8);
        }

        return id;
    }
}