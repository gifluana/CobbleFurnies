import os
import json

def generate_blockstate(color):
    multipart = []
    for facing, y in zip(["north", "east", "south", "west"], [0, 90, 180, 270]):
        for hinge in ["left", "right"]:
            # Base cabinet model
            multipart.append({
                "when": {"facing": facing, "hinge": hinge},
                "apply": {
                    "model": f"cobblefurnies:block/upper_cabinet/{color}_{hinge}",
                    "y": y
                }
            })

            # Solid door closed/open
            for is_open in [False, True]:
                multipart.append({
                    "when": {
                        "facing": facing,
                        "hinge": hinge,
                        "has_glass": False,
                        "open": is_open
                    },
                    "apply": {
                        "model": f"cobblefurnies:block/upper_cabinet/{color}_{hinge}_door" + ("_open" if is_open else ""),
                        "y": y
                    }
                })

            # Glass door closed/open (already modeled)
            for is_open in [False, True]:
                multipart.append({
                    "when": {
                        "facing": facing,
                        "hinge": hinge,
                        "has_glass": True,
                        "open": is_open
                    },
                    "apply": {
                        "model": f"cobblefurnies:block/upper_cabinet/glass_{hinge}_door" + ("_open" if is_open else ""),
                        "y": y
                    }
                })
    return {"multipart": multipart}

def generate_cabinet_model(color, hinge):
    return {
        "parent": f"cobblefurnies:block/template/upper_cabinet_{hinge}",
        "textures": {
            "cabinet": f"cobblefurnies:block/upper_cabinet/{color}",
            "particle": f"cobblefurnies:block/particles/color/{color}"
        }
    }

def generate_door_model(color, hinge, is_open):
    return {
        "parent": f"cobblefurnies:block/template/upper_cabinet_{hinge}_door" + ("_open" if is_open else ""),
        "textures": {
            "door": f"cobblefurnies:block/upper_cabinet/{color}_door"
        }
    }

def generate_item_model(color):
    return {
        "parent": "cobblefurnies:block/template/upper_cabinet_full",
        "textures": {
            "door": f"cobblefurnies:block/upper_cabinet/{color}_door",
            "cabinet": f"cobblefurnies:block/upper_cabinet/{color}"
        }
    }

def save_json(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

def main():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_path = os.path.join(script_dir, "colors.json")

    if not os.path.exists(colors_path):
        print("colors.json not found.")
        return

    with open(colors_path, "r", encoding="utf-8") as f:
        colors = json.load(f)

    base_dir = os.path.join(script_dir, "cobblefurnies")

    for color in colors:
        print(f"🔧 Gerando arquivos do Upper Cabinet: {color}")

        # Blockstate
        save_json(os.path.join(base_dir, f"blockstates/{color}_upper_cabinet.json"), generate_blockstate(color))

        # Block models
        for hinge in ["left", "right"]:
            save_json(os.path.join(base_dir, f"models/block/upper_cabinet/{color}_{hinge}.json"), generate_cabinet_model(color, hinge))
            for is_open in [False, True]:
                save_json(
                    os.path.join(base_dir, f"models/block/upper_cabinet/{color}_{hinge}_door" + ("_open" if is_open else "") + ".json"),
                    generate_door_model(color, hinge, is_open)
                )

        # Item model
        save_json(os.path.join(base_dir, f"models/item/{color}_upper_cabinet.json"), generate_item_model(color))

if __name__ == "__main__":
    main()
