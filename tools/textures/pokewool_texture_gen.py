import os
import json
from PIL import Image

def load_palette(json_file):
    """Load the color palette from JSON."""
    with open(json_file, "r", encoding="utf-8") as f:
        return json.load(f)

def recolor_images(images_folder, color_palette, output_folder):
    """Replace colors in light_blue variants using the palette and save new versions."""
    texture_names = ["center", "empty", "horizontal", "vertical", "particle"]
    
    reference_images = {name: Image.open(os.path.join(images_folder, f"light_blue_{name}.png")).convert("RGBA") for name in texture_names}
    reference_colors = [tuple(int(color[i:i+2], 16) for i in (1, 3, 5)) for color in color_palette["light_blue"]]
    
    for color_name, target_colors in color_palette.items():
        if color_name == "light_blue":
            continue
        
        target_colors_rgb = [tuple(int(color[i:i+2], 16) for i in (1, 3, 5)) for color in target_colors]
        
        for name in texture_names:
            img_path = os.path.join(images_folder, f"light_blue_{name}.png")
            if not os.path.exists(img_path):
                print(f"Error: '{img_path}' not found.")
                continue
            
            new_img = reference_images[name].copy()
            pixels = new_img.load()
            width, height = new_img.size
            
            for x in range(width):
                for y in range(height):
                    original_color = pixels[x, y][:3]  # Ignore alpha
                    if original_color in reference_colors:
                        index = reference_colors.index(original_color)
                        new_color = target_colors_rgb[index] + (pixels[x, y][3],)  # Preserve alpha
                        pixels[x, y] = new_color
            
            new_image_path = os.path.join(output_folder, f"{color_name}_{name}.png")
            new_img.save(new_image_path)
            print(f"Saved: {new_image_path}")

def apply_palette_to_images(images_folder, json_palette):
    """Apply palette colors to all images."""
    color_palette = load_palette(json_palette)
    recolor_images(images_folder, color_palette, images_folder)

# Example usage
images_folder = "pokewool"  # Folder containing images
json_palette = "concrete_palette.json"  # JSON file with the color palette
apply_palette_to_images(images_folder, json_palette)
