import os
import json

def extract_color_from_path(path: str) -> str | None:
    if not path.startswith("cobblefurnies:block/"):
        return None
    last_part = path.split("/")[-1]
    if last_part.endswith("_black"):
        return last_part[:-6]  # remove "_black"
    return last_part

def process_json_file(filepath):
    with open(filepath, "r", encoding="utf-8") as f:
        try:
            data = json.load(f)
        except json.JSONDecodeError:
            print(f"[ERRO] JSON inválido: {filepath}")
            return

    textures = data.get("textures", {})
    if "particle" in textures:
        print(f"[SKIP] Já possui 'particle': {filepath}")
        return

    texture_keys = ["0", "1", "2"]
    selected_path = None

    for key in texture_keys:
        path = textures.get(key)
        if path and isinstance(path, str):
            selected_path = path
            break

    if not selected_path:
        print(f"[AVISO] Nenhuma textura válida encontrada em: {filepath}")
        return

    color = extract_color_from_path(selected_path)
    if not color:
        print(f"[ERRO] Não foi possível extrair cor de: {selected_path}")
        return

    textures["particle"] = f"cobblefurnies:block/particles/table/{color}"
    data["textures"] = textures

    with open(filepath, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4, ensure_ascii=False)

    print(f"[OK] Atualizado: {filepath}")


if __name__ == "__main__":
    current_dir = os.getcwd()
    for filename in os.listdir(current_dir):
        if filename.endswith(".json"):
            process_json_file(os.path.join(current_dir, filename))
