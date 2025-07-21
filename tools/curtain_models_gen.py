import os
import json

# Cores do arquivo colors.json (ex: ["red", "blue", "light_blue"])
def get_block_model(color, part, section, open_state):
    texture = f"cobblefurnies:block/curtain/{color}_closed_{section}"
    if open_state:
        if part == "middle":
            texture = f"cobblefurnies:block/curtain/{color}_middle_open"
        else:
            texture = f"cobblefurnies:block/curtain/{color}_{part}_open_{section}"
    return {
        "parent": f"cobblefurnies:block/template/curtain_{section}",
        "textures": {"curtain": texture}
    }

def get_block_model_full(color):
    return {
        "parent": "cobblefurnies:block/template/curtain_full",
        "textures": {
            "top": f"cobblefurnies:block/curtain/{color}_closed_top",
            "bottom": f"cobblefurnies:block/curtain/{color}_closed_bottom"
        }
    }

def get_item_model(color):
    return {
        "parent": f"cobblefurnies:block/curtain/{color}_curtain_full"
    }

def get_lang_entry(color):
    return {f"item.cobblefurnies.{color}_curtain": ""}

def get_blockstate(color):
    variants = {}
    facings = ["north", "east", "south", "west"]
    horizontal_parts = ["left", "middle", "right", "single"]
    vertical_parts = ["top", "bottom"]
    open_states = [False, True]
    hinges = ["left", "right"]
    y_rotation = {"north": 0, "east": 90, "south": 180, "west": 270}

    def model_name(part, vert, open_):
        suffix = "_open" if open_ else ""
        return f"cobblefurnies:block/curtain/{color}_curtain_{part}_{vert}{suffix}"

    for facing in facings:
        y = y_rotation[facing]
        for hp in horizontal_parts:
            for vp in vertical_parts:
                for open_ in open_states:
                    key_base = f"facing={facing},horizontal_part={hp},vertical_part={vp},open={str(open_).lower()}"
                    if hp == "single":
                        for hinge in hinges:
                            model = model_name("left" if hinge == "left" else "right", vp, open_)
                            key = f"{key_base},hinge={hinge}"
                            variants[key] = {"model": model}
                            if y: variants[key]["y"] = y
                    else:
                        if hp == "middle" and vp == "bottom" and open_:
                            model = "minecraft:block/air"
                        else:
                            model = model_name(hp, vp, open_)
                        key = key_base
                        variants[key] = {"model": model}
                        if y: variants[key]["y"] = y
    return {"variants": variants}

def save_json(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

def generate_files():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")
    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    base = os.path.join(script_dir, "cobblefurnies")
    lang_entries = {}

    for color in colors:
        # Item
        item_model = os.path.join(base, f"models/item/{color}_curtain.json")
        save_json(item_model, get_item_model(color))

        # Full block model
        block_model_full = os.path.join(base, f"models/block/curtain/{color}_curtain_full.json")
        save_json(block_model_full, get_block_model_full(color))

        # Parts
        for part in ["left", "middle", "right"]:
            for section in ["top", "bottom"]:
                for open_ in [False, True]:
                    if part == "middle" and section == "bottom" and open_:
                        continue
                    name = f"{color}_curtain_{part}_{section}"
                    if open_:
                        name += "_open"
                    model_path = os.path.join(base, f"models/block/curtain/{name}.json")
                    save_json(model_path, get_block_model(color, part, section, open_))

        # Blockstate
        blockstate_path = os.path.join(base, f"blockstates/{color}_curtain.json")
        save_json(blockstate_path, get_blockstate(color))

        # Lang
        lang_entries.update(get_lang_entry(color))

    # Lang file
    lang_path = os.path.join(base, "lang/en_us.json")
    save_json(lang_path, lang_entries)

if __name__ == "__main__":
    generate_files()
