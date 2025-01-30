import os
import json

# Function to generate blockstate JSON
def get_blockstate(color):
    return {
        "variants": {
            "facing=north,open=false": {"model": f"cobblefurnies:block/drawer/{color}"},
            "facing=east,open=false": {"model": f"cobblefurnies:block/drawer/{color}", "y": 90},
            "facing=south,open=false": {"model": f"cobblefurnies:block/drawer/{color}", "y": 180},
            "facing=west,open=false": {"model": f"cobblefurnies:block/drawer/{color}", "y": 270},
            "facing=north,open=true": {"model": f"cobblefurnies:block/drawer/{color}_open"},
            "facing=east,open=true": {"model": f"cobblefurnies:block/drawer/{color}_open", "y": 90},
            "facing=south,open=true": {"model": f"cobblefurnies:block/drawer/{color}_open", "y": 180},
            "facing=west,open=true": {"model": f"cobblefurnies:block/drawer/{color}_open", "y": 270}
        }
    }

# Function to generate block model JSON (closed drawer)
def get_block_model(color):
    return {
        "parent": "cobblefurnies:block/template/drawer",
        "textures": {
            "0": f"cobblefurnies:block/drawer/{color}"
        }
    }

# Function to generate block model JSON (open drawer)
def get_block_model_open(color):
    return {
        "parent": "cobblefurnies:block/template/drawer_open",
        "textures": {
            "0": f"cobblefurnies:block/drawer/{color}"
        }
    }

# Function to generate item model JSON
def get_item_model(color):
    return {
        "parent": f"cobblefurnies:block/drawer/{color}"
    }

# Function to save JSON data to a file
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Main function
def generate_files():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")

    # Check if colors.json exists
    if not os.path.exists(colors_file):
        print("Error: colors.json not found.")
        return

    # Load colors from JSON file
    try:
        with open(colors_file, "r", encoding="utf-8") as f:
            colors = json.load(f)
    except json.JSONDecodeError:
        print("Error: Invalid JSON format in colors.json.")
        return

    # Base directory for cobblefurnies
    base_dir = os.path.join(script_dir, "cobblefurnies")

    # Generate files for each color
    for color in colors:
        print(f"Generating drawer files for color: {color}")

        # Blockstate
        save_json_file(os.path.join(base_dir, f"blockstates/{color}_drawer.json"), get_blockstate(color))

        # Block Models
        save_json_file(os.path.join(base_dir, f"models/block/drawer/{color}.json"), get_block_model(color))
        save_json_file(os.path.join(base_dir, f"models/block/drawer/{color}_open.json"), get_block_model_open(color))

        # Item Model
        save_json_file(os.path.join(base_dir, f"models/item/{color}_drawer.json"), get_item_model(color))

if __name__ == "__main__":
    generate_files()
