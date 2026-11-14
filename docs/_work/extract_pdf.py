import fitz, os, json
doc = fitz.open(r"f:\DAM\PROYECTO\Gestion Guarderia\descarga.pdf")
out = []
for i, page in enumerate(doc):
    t = page.get_text("text")
    imgs = page.get_images(full=True)
    out.append({"page": i+1, "w": page.rect.width, "h": page.rect.height, "images": len(imgs), "text": t})
    pix = page.get_pixmap(dpi=100)
    pix.save(rf"f:\DAM\PROYECTO\Gestion Guarderia\docs\_work\pdf_pages\page_{i+1:02d}.png")
open(r"f:\DAM\PROYECTO\Gestion Guarderia\docs\_work\pdf_text.json", "w", encoding="utf-8").write(json.dumps(out, ensure_ascii=False, indent=2))
print("pages:", len(out))
for p in out:
    print("=== PAGE", p["page"], "size", round(p["w"]), round(p["h"]), "images", p["images"], "chars", len(p["text"]))
    print(p["text"][:1500])
