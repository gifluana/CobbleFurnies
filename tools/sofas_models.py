import os
import json

# Define the base directory for generated files
BASE_DIR = "cobblefurnies"

# Blockstate Template
def get_blockstate(color):
    return {
        "variants": {
            f"facing={facing},type={type_}": {
                "model": f"cobblefurnies:block/sofa/{color}{'_' + type_ if type_ != 'single' else ''}",
                "y": rotation
            }
            for facing, rotation in [("north", 0), ("east", 90), ("south", 180), ("west", 270)]
            for type_ in ["single", "left", "right", "middle", "inner_left", "inner_right", "outer_left", "outer_right"]
        }
    }

# Block Model Template
def get_block_model(color, type_=""):
    return {
        "parent": f"cobblefurnies:block/template/sofa{f'_{type_}' if type_ else ''}",
        "textures": {
            "1": f"cobblefurnies:block/sofa/{color}"
        }
    }

# Item Model Template
def get_item_model(color):
    return {
        "parent": f"cobblefurnies:block/sofa/{color}"
    }

# Function to save JSON data to a file
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Main function
def generate_sofa_files():
    # Read colors from the JSON file
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    for color in colors:
        # Generate Blockstate file
        blockstate_path = os.path.join(BASE_DIR, f"blockstates/{color}_sofa.json")
        save_json_file(blockstate_path, get_blockstate(color))

        # Generate Block Models
        for model_type in ["", "inner", "left", "middle", "outer", "right"]:
            model_path = os.path.join(BASE_DIR, f"models/block/sofa/{color}{'_' + model_type if model_type else ''}.json")
            save_json_file(model_path, get_block_model(color, model_type))

        # Generate Item Model
        item_model_path = os.path.join(BASE_DIR, f"models/item/{color}_sofa.json")
        save_json_file(item_model_path, get_item_model(color))

    print("Sofa data generation completed!")

# Run the script
if __name__ == "__main__":
    generate_sofa_files()
