package persistence;

import org.json.JSONObject;

// referenced JsonSerializationDemo
public interface Writeable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
