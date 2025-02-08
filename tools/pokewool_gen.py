import os
import json

def get_blockstate(color, is_carpet=False):
    return {
        "variants": {
            "": {
                "model": "minecraft:block/air"
            }
        },
        "athena:loader": "athena:carpet_ctm" if is_carpet else "athena:ctm",
        "ctm_textures": {
            "center": f"cobblefurnies:block/blocks/pokewool/{color}_center",
            "empty": f"cobblefurnies:block/blocks/pokewool/{color}_empty",
            "horizontal": f"cobblefurnies:block/blocks/pokewool/{color}_horizontal",
            "particle": f"cobblefurnies:block/blocks/pokewool/{color}_particle",
            "vertical": f"cobblefurnies:block/blocks/pokewool/{color}_vertical"
        }
    }

def get_item_model(color, is_carpet=False):
    return {
        "parent": "minecraft:block/carpet" if is_carpet else "minecraft:block/cube_all",
        "textures": {
            "wool" if is_carpet else "all": f"cobblefurnies:block/blocks/pokewool/{color}_particle"
        }
    }

def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

def generate_files():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")
    
    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)
    
    base_dir = os.path.join(script_dir, "cobblefurnies")
    
    for color in colors:
        # Blockstate for poke wool
        save_json_file(os.path.join(base_dir, f"blockstates/{color}_poke_wool.json"), get_blockstate(color))
        
        # Blockstate for poke wool carpet
        save_json_file(os.path.join(base_dir, f"blockstates/{color}_poke_wool_carpet.json"), get_blockstate(color, is_carpet=True))
        
        # Item model for poke wool
        save_json_file(os.path.join(base_dir, f"models/item/{color}_poke_wool.json"), get_item_model(color))
        
        # Item model for poke wool carpet
        save_json_file(os.path.join(base_dir, f"models/item/{color}_poke_wool_carpet.json"), get_item_model(color, is_carpet=True))

if __name__ == "__main__":
    generate_files()