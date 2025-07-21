import os
import json

# Define the templates for the files
def get_item_model(color):
    return {
        "parent": f"cobblefurnies:block/armchair/{color}"
    }

def get_block_model(color):
    return {
        "parent": "cobblefurnies:block/template/armchair",
        "textures": {
            "1": f"cobblefurnies:block/armchair/{color}"
        }
    }

def get_blockstate(color):
    return {
        "variants": {
            "facing=north": {
                "model": f"cobblefurnies:block/armchair/{color}"
            },
            "facing=east": {
                "model": f"cobblefurnies:block/armchair/{color}",
                "y": 90
            },
            "facing=south": {
                "model": f"cobblefurnies:block/armchair/{color}",
                "y": 180
            },
            "facing=west": {
                "model": f"cobblefurnies:block/armchair/{color}",
                "y": 270
            }
        }
    }

def get_lang_entry(color):
    return {f"item.cobblefurnies.{color}_armchair": ""}

# Function to save JSON data to a file
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Main function
def generate_files():
    # Read colors from the JSON file in the same directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    # Base directory for cobblefurnies
    base_dir = os.path.join(script_dir, "cobblefurnies")

    # Generate files for each color
    lang_entries = {}
    for color in colors:
        # Item model
        item_model_path = os.path.join(base_dir, f"models/item/{color}_armchair.json")
        save_json_file(item_model_path, get_item_model(color))

        # Block model
        block_model_path = os.path.join(base_dir, f"models/block/armchair/{color}.json")
        save_json_file(block_model_path, get_block_model(color))

        # Blockstate
        blockstate_path = os.path.join(base_dir, f"blockstates/{color}_armchair.json")
        save_json_file(blockstate_path, get_blockstate(color))

        # Lang entry
        lang_entries.update(get_lang_entry(color))

    # Save the lang file
    lang_path = os.path.join(base_dir, "lang/en_us.json")
    save_json_file(lang_path, lang_entries)

if __name__ == "__main__":
    generate_files()
