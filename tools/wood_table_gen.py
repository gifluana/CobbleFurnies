import os
import json

# Function to generate blockstate
def get_blockstate(wood):
    return {
        "variants": {
            "east=false,north=false,south=false,west=false": {
                "model": f"cobblefurnies:block/table/{wood}"
            },
            "east=false,north=false,south=false,west=true": {
                "model": f"cobblefurnies:block/table/{wood}_west"
            },
            "east=false,north=false,south=true,west=false": {
                "model": f"cobblefurnies:block/table/{wood}_south"
            },
            "east=false,north=false,south=true,west=true": {
                "model": f"cobblefurnies:block/table/{wood}_south_west"
            },
            "east=false,north=true,south=false,west=false": {
                "model": f"cobblefurnies:block/table/{wood}_north"
            },
            "east=false,north=true,south=false,west=true": {
                "model": f"cobblefurnies:block/table/{wood}_west_north"
            },
            "east=false,north=true,south=true,west=false": {
                "model": f"cobblefurnies:block/table/{wood}_north_south"
            },
            "east=false,north=true,south=true,west=true": {
                "model": f"cobblefurnies:block/table/{wood}_south_west_north"
            },
            "east=true,north=false,south=false,west=false": {
                "model": f"cobblefurnies:block/table/{wood}_east"
            },
            "east=true,north=false,south=false,west=true": {
                "model": f"cobblefurnies:block/table/{wood}_east_west"
            },
            "east=true,north=false,south=true,west=false": {
                "model": f"cobblefurnies:block/table/{wood}_east_south"
            },
            "east=true,north=false,south=true,west=true": {
                "model": f"cobblefurnies:block/table/{wood}_east_south_west"
            },
            "east=true,north=true,south=false,west=false": {
                "model": f"cobblefurnies:block/table/{wood}_north_east"
            },
            "east=true,north=true,south=false,west=true": {
                "model": f"cobblefurnies:block/table/{wood}_west_north_east"
            },
            "east=true,north=true,south=true,west=false": {
                "model": f"cobblefurnies:block/table/{wood}_north_east_south"
            },
            "east=true,north=true,south=true,west=true": {
                "model": f"cobblefurnies:block/table/{wood}_north_east_south_west"
            }
        }
    }

# Function to generate block models
def get_block_model(wood, suffix=""):
    return {
        "parent": f"cobblefurnies:block/template/table{suffix}",
        "textures": {
            "1": f"cobblefurnies:block/table/{wood}"
        }
    }

# Function to generate item model
def get_item_model(wood):
    return {
        "parent": "cobblefurnies:block/template/table",
        "textures": {
            "1": f"cobblefurnies:block/table/{wood}"
        }
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
    woods_file = os.path.join(script_dir, "woods.json")

    with open(woods_file, "r", encoding="utf-8") as f:
        woods = json.load(f)

    # Base directory for cobblefurnies
    base_dir = os.path.join(script_dir, "cobblefurnies")

    # Generate files for each wood type
    for wood in woods:
        # Blockstate
        blockstate_path = os.path.join(base_dir, f"blockstates/{wood}_table.json")
        save_json_file(blockstate_path, get_blockstate(wood))

        # Block models
        suffixes = [
            "", "_east", "_east_south", "_east_south_west", "_east_west", "_north", "_north_east",
            "_north_east_south", "_north_east_south_west", "_north_south", "_south", "_south_west",
            "_south_west_north", "_west", "_west_north", "_west_north_east"
        ]
        for suffix in suffixes:
            block_model_path = os.path.join(base_dir, f"models/block/table/{wood}{suffix}.json")
            save_json_file(block_model_path, get_block_model(wood, suffix))

        # Item model
        item_model_path = os.path.join(base_dir, f"models/item/{wood}_table.json")
        save_json_file(item_model_path, get_item_model(wood))

if __name__ == "__main__":
    generate_files()
