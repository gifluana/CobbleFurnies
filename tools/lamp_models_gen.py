import os
import json

def get_lamp_model(color: str, mount: str, lit: bool):
    suffix = "on" if lit else "off"
    return {
        "parent": f"cobblefurnies:block/template/lamp_{mount}",
        "textures": {
            "1": f"cobblefurnies:block/lamp/{color}_{suffix}",
            "particle": f"cobblefurnies:block/particles/color/{color}"
        }
    }

def get_item_model(color: str):
    return {
        "parent": f"cobblefurnies:block/lamp/{color}_floor_on"
    }

def get_blockstate(color: str):
    variants = {}
    directions = ["north", "east", "south", "west"]
    y_rotation = {"north": 0, "east": 90, "south": 180, "west": 270}

    # Floor models
    for lit in [False, True]:
        key = f"mount_type=floor,lit={str(lit).lower()}"
        model = f"cobblefurnies:block/lamp/{color}_floor_{'on' if lit else 'off'}"
        variants[key] = { "model": model }

    # Wall models (rotated by facing)
    for dir in directions:
        y = y_rotation[dir]
        for lit in [False, True]:
            key = f"mount_type=wall,facing={dir},lit={str(lit).lower()}"
            model = f"cobblefurnies:block/lamp/{color}_wall_{'on' if lit else 'off'}"
            entry = { "model": model }
            if y != 0:
                entry["y"] = y
            variants[key] = entry

    return { "variants": variants }

def save_json(path: str, data: dict):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

def generate_lamp_files():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    base = os.path.join(script_dir, "cobblefurnies")

    for color in colors:
        # Blockstate
        blockstate_path = os.path.join(base, f"blockstates/{color}_lamp.json")
        save_json(blockstate_path, get_blockstate(color))

        # Item model
        item_model_path = os.path.join(base, f"models/item/{color}_lamp.json")
        save_json(item_model_path, get_item_model(color))

        # Block models
        for mount in ["floor", "wall"]:
            for lit in [False, True]:
                suffix = "on" if lit else "off"
                model_path = os.path.join(base, f"models/block/lamp/{color}_{mount}_{suffix}.json")
                save_json(model_path, get_lamp_model(color, mount, lit))

if __name__ == "__main__":
    generate_lamp_files()
