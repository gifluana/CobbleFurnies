import os
import json

# Function to generate block models
def get_block_model(wood_type, shape):
    return {
        "parent": f"cobblefurnies:block/template/cabinet_{shape}",
        "textures": {
            "2": f"cobblefurnies:block/cabinet/{wood_type}"
        }
    }

# Function to generate item model
def get_item_model(wood_type):
    return {
        "parent": f"cobblefurnies:block/cabinet/{wood_type}_closed_left"
    }

# Function to save JSON file
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Main function
def generate_files():
    # Read wood types from the JSON file in the same directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    wood_types_file = os.path.join(script_dir, "woods.json")

    with open(wood_types_file, "r", encoding="utf-8") as f:
        wood_types = json.load(f)

    # Base directory for cobblefurnies
    base_dir = os.path.join(script_dir, "cobblefurnies")
    shapes = ["closed_left", "closed_right", "open_left", "open_right"]

    # Generate files for each wood type
    for wood_type in wood_types:
        # Block models
        for shape in shapes:
            block_model_path = os.path.join(base_dir, f"models/block/cabinet/{wood_type}_{shape}.json")
            save_json_file(block_model_path, get_block_model(wood_type, shape))

        # Item model
        item_model_path = os.path.join(base_dir, f"models/item/{wood_type}_cabinet.json")
        save_json_file(item_model_path, get_item_model(wood_type))

if __name__ == "__main__":
    generate_files()
