import os
import json

# Gera o blockstate com todas as variantes de shape + facing
def get_blockstate(wood):
    variants = {}
    shapes = ["single", "left", "right", "middle"]
    facings = {
        "north": 0,
        "east": 90,
        "south": 180,
        "west": 270
    }

    for shape in shapes:
        for facing, y in facings.items():
            key = f"facing={facing},shape={shape}"
            entry = {
                "model": f"cobblefurnies:block/wall_support/{wood}_{shape}"
            }
            if y != 0:
                entry["y"] = y
            variants[key] = entry

    return {"variants": variants}

# Gera modelo de bloco
def get_block_model(wood, shape):
    return {
        "parent": f"cobblefurnies:block/template/wall_support_{shape}",
        "textures": {
            "0": f"cobblefurnies:block/wall_support/{wood}",
            "particle": f"cobblefurnies:block/particles/table/{wood}"
        }
    }

# Gera modelo de item
def get_item_model(wood):
    return {
        "parent": f"cobblefurnies:block/wall_support/{wood}_single"
    }

# Salva JSON em arquivo
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Função principal
def generate_files():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    woods_file = os.path.join(script_dir, "woods.json")

    with open(woods_file, "r", encoding="utf-8") as f:
        woods = json.load(f)

    base_dir = os.path.join(script_dir, "cobblefurnies")

    for wood in woods:
        # Blockstate
        blockstate_path = os.path.join(base_dir, f"blockstates/{wood}_wall_support.json")
        save_json_file(blockstate_path, get_blockstate(wood))

        # Block models
        for shape in ["single", "left", "right", "middle"]:
            model_path = os.path.join(base_dir, f"models/block/wall_support/{wood}_{shape}.json")
            save_json_file(model_path, get_block_model(wood, shape))

        # Item model
        item_model_path = os.path.join(base_dir, f"models/item/{wood}_wall_support.json")
        save_json_file(item_model_path, get_item_model(wood))

if __name__ == "__main__":
    generate_files()
