import os
import json

def get_blockstate(color):
    variants = {}
    facings = ["north", "south", "east", "west"]
    horizontal_parts = ["left", "middle", "right", "single"]
    vertical_parts = ["top", "bottom"]
    open_states = [False, True]
    hinges = ["left", "right"]

    def get_model_name(base, open_state):
        return f"{base}_open" if open_state else base

    for facing in facings:
        y_rotation = {
            "north": 0,
            "east": 90,
            "south": 180,
            "west": 270
        }[facing]

        for hp in horizontal_parts:
            for vp in vertical_parts:
                for open_state in open_states:
                    key_base = f"facing={facing},horizontal_part={hp},vertical_part={vp},open={str(open_state).lower()}"
                    model_base = f"cobblefurnies:block/curtain/{color}_curtain_{hp}_{vp}"
                    if hp == "single":
                        for hinge in hinges:
                            model_name = model_base.replace("single", hinge)
                            key = f"{key_base},hinge={hinge}"
                            variants[key] = {
                                "model": get_model_name(model_name, open_state)
                            }
                            if y_rotation:
                                variants[key]["y"] = y_rotation
                    else:
                        # air block if middle bottom open
                        if hp == "middle" and vp == "bottom" and open_state:
                            model = "minecraft:block/air"
                        else:
                            model = get_model_name(model_base, open_state)
                        key = key_base
                        variants[key] = {
                            "model": model
                        }
                        if y_rotation:
                            variants[key]["y"] = y_rotation
    return {"variants": variants}

def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

def generate_curtain_blockstates():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    for color in colors:
        blockstate_path = os.path.join(script_dir, "cobblefurnies", "blockstates", f"{color}_curtain.json")
        save_json_file(blockstate_path, get_blockstate(color))

if __name__ == "__main__":
    generate_curtain_blockstates()
