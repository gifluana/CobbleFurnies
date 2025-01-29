import os
import json

# Function to generate blockstate
def get_blockstate(color):
    shapes = [
        "default", "inner_corner_left", "inner_corner_right",
        "outer_corner_left", "outer_corner_right"
    ]
    directions = ["north", "east", "south", "west"]
    rotations = {"north": 0, "east": 90, "south": 180, "west": 270}

    blockstate = {"multipart": []}
    for direction in directions:
        for shape in shapes:
            blockstate["multipart"].append({
                "when": {"facing": direction, "shape": shape},
                "apply": {"model": f"cobblefurnies:block/cabinetry/{color}_{shape}", "y": rotations[direction]}
            })
    return blockstate

# Function to generate block models
def get_block_model(color, shape):
    return {
        "parent": f"cobblefurnies:block/template/cabinetry_{shape}",
        "textures": {
            "2": f"cobblefurnies:block/cabinetry/{color}"
        }
    }

# Function to generate item model
def get_item_model(color):
    return {
        "parent": f"cobblefurnies:block/cabinetry/{color}_default"
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
    colors_file = os.path.join(script_dir, "colors.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    # Base directory for cobblefurnies
    base_dir = os.path.join(script_dir, "cobblefurnies")
    shapes = [
        "default", "inner_corner_left", "inner_corner_right",
        "outer_corner_left", "outer_corner_right"
    ]

    # Generate files for each color
    for color in colors:
        # Blockstate
        blockstate_path = os.path.join(base_dir, f"blockstates/{color}_cabinetry.json")
        save_json_file(blockstate_path, get_blockstate(color))

        # Block models
        for shape in shapes:
            block_model_path = os.path.join(base_dir, f"models/block/cabinetry/{color}_{shape}.json")
            save_json_file(block_model_path, get_block_model(color, shape))

        # Item model
        item_model_path = os.path.join(base_dir, f"models/item/{color}_cabinetry.json")
        save_json_file(item_model_path, get_item_model(color))

if __name__ == "__main__":
    generate_files()
