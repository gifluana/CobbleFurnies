import os
import json

# Function to generate blockstate

def get_blockstate(color):
    return {
        "variants": {
            "east=false,north=false,south=false,west=false": {
                "model": f"cobblefurnies:block/table/{color}"
            },
            "east=false,north=false,south=false,west=true": {
                "model": f"cobblefurnies:block/table/{color}_west"
            },
            "east=false,north=false,south=true,west=false": {
                "model": f"cobblefurnies:block/table/{color}_south"
            },
            "east=false,north=false,south=true,west=true": {
                "model": f"cobblefurnies:block/table/{color}_south_west"
            },
            "east=false,north=true,south=false,west=false": {
                "model": f"cobblefurnies:block/table/{color}_north"
            },
            "east=false,north=true,south=false,west=true": {
                "model": f"cobblefurnies:block/table/{color}_west_north"
            },
            "east=false,north=true,south=true,west=false": {
                "model": f"cobblefurnies:block/table/{color}_north_south"
            },
            "east=false,north=true,south=true,west=true": {
                "model": f"cobblefurnies:block/table/{color}_south_west_north"
            },
            "east=true,north=false,south=false,west=false": {
                "model": f"cobblefurnies:block/table/{color}_east"
            },
            "east=true,north=false,south=false,west=true": {
                "model": f"cobblefurnies:block/table/{color}_east_west"
            },
            "east=true,north=false,south=true,west=false": {
                "model": f"cobblefurnies:block/table/{color}_east_south"
            },
            "east=true,north=false,south=true,west=true": {
                "model": f"cobblefurnies:block/table/{color}_east_south_west"
            },
            "east=true,north=true,south=false,west=false": {
                "model": f"cobblefurnies:block/table/{color}_north_east"
            },
            "east=true,north=true,south=false,west=true": {
                "model": f"cobblefurnies:block/table/{color}_west_north_east"
            },
            "east=true,north=true,south=true,west=false": {
                "model": f"cobblefurnies:block/table/{color}_north_east_south"
            },
            "east=true,north=true,south=true,west=true": {
                "model": f"cobblefurnies:block/table/{color}_north_east_south_west"
            }
        }
    }

# Function to generate block models
def get_block_model(color, suffix=""):
    return {
        "parent": f"cobblefurnies:block/template/table{suffix}",
        "textures": {
            "1": f"cobblefurnies:block/table/{color}"
        }
    }

# Function to generate item model
def get_item_model(color):
    return {
        "parent": "cobblefurnies:block/template/table",
        "textures": {
            "1": f"cobblefurnies:block/table/{color}"
        }
    }

# Function to save JSON file
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Main function
def generate_files():
    # Read colors from the JSON file in the same directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "woods.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    # Base directory for cobblefurnies
    base_dir = os.path.join(script_dir, "cobblefurnies")

    # Generate files for each color
    for color in colors:
        # Blockstate
        blockstate_path = os.path.join(base_dir, f"blockstates/{color}_table.json")
        save_json_file(blockstate_path, get_blockstate(color))

        # Block models
        suffixes = [
            "", "_east", "_east_south", "_east_south_west", "_east_west", "_north", "_north_east",
            "_north_east_south", "_north_east_south_west", "_north_south", "_south", "_south_west",
            "_south_west_north", "_west", "_west_north", "_west_north_east"
        ]
        for suffix in suffixes:
            block_model_path = os.path.join(base_dir, f"models/block/table/{color}{suffix}.json")
            save_json_file(block_model_path, get_block_model(color, suffix))

        # Item model
        item_model_path = os.path.join(base_dir, f"models/item/{color}_table.json")
        save_json_file(item_model_path, get_item_model(color))

if __name__ == "__main__":
    generate_files()
