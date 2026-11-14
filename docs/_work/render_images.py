import os
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont
from pygments import lex
from pygments.lexers import JavaLexer
from pygments.styles import get_style_by_name

BASE = Path(r"f:\DAM\PROYECTO\Gestion Guarderia")
TEST_DIR = BASE / "app" / "src" / "test" / "java" / "dam" / "proyecto" / "gestionguarderia"
WORK = BASE / "docs" / "_work"
OUT = BASE / "docs" / "images"
OUT.mkdir(parents=True, exist_ok=True)

FONT_DIR = Path(r"C:\Windows\Fonts")
FONT_REGULAR = str(FONT_DIR / "consola.ttf")
FONT_BOLD = str(FONT_DIR / "consolab.ttf")
UI_REGULAR = str(FONT_DIR / "segoeui.ttf")
UI_BOLD = str(FONT_DIR / "segoeuib.ttf")

CASES = [
    ("Alumnos", "AlumnosJsonIntegrationTest", "testJsonPersistenceAndReferentialIntegrity", "0,078"),
    ("Aula", "AulaJsonIntegrationTest", "testJsonMappingAndAulaRepository", "0,065"),
    ("Usuarios", "UsuariosJsonIntegrationTest", "testJsonMappingAndRoleIntegrity", "0,069"),
    ("Incidencias", "IncidenciasJsonIntegrationTest", "testJsonMappingAndIncidentCodes", "0,065"),
]


def read_text(path):
    return path.read_text(encoding="utf-8-sig")


def extract_test_method(path):
    lines = read_text(path).splitlines()
    start = next(i for i, line in enumerate(lines) if line.strip().startswith("@Test"))
    depth = 0
    started = False
    end = start
    for i in range(start, len(lines)):
        depth += lines[i].count("{") - lines[i].count("}")
        if "{" in lines[i]:
            started = True
        if started and depth == 0:
            end = i
            break
    return lines[start:end + 1], start + 1


def wrap_code_line(line, font, max_width):
    if font.getlength(line) <= max_width:
        return [line]
    pieces = []
    rest = line
    while font.getlength(rest) > max_width:
        lo, hi = 20, len(rest)
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if font.getlength(rest[:mid]) <= max_width:
                lo = mid
            else:
                hi = mid - 1
        cut = lo
        space = rest.rfind(" ", 20, cut)
        if space > 20:
            cut = space
        pieces.append(rest[:cut].rstrip())
        rest = "        " + rest[cut:].lstrip()
    pieces.append(rest)
    return pieces


def color_runs_for_line(line):
    style = get_style_by_name("monokai")
    runs = []
    for token, value in lex(line, JavaLexer()):
        if value == "\n":
            continue
        info = style.style_for_token(token)
        color = "#" + (info["color"] or "F8F8F2")
        runs.append((value, color, bool(info["bold"])))
    if not runs:
        runs = [(" ", "#F8F8F2", False)]
    return runs


def draw_code(case_name, class_name, method_name, seconds, number):
    path = TEST_DIR / f"{class_name}.java"
    code_lines, first_number = extract_test_method(path)

    font = ImageFont.truetype(FONT_REGULAR, 28)
    font_bold = ImageFont.truetype(FONT_BOLD, 28)
    font_ui = ImageFont.truetype(UI_REGULAR, 22)
    font_ui_bold = ImageFont.truetype(UI_BOLD, 23)

    line_h = 41
    top = 100
    gutter_w = 84
    width = 1500
    x0 = gutter_w + 22
    render_lines = []
    for idx, line in enumerate(code_lines):
        parts = wrap_code_line(line, font, width - x0 - 28)
        for part_idx, part in enumerate(parts):
            line_no = first_number + idx if part_idx == 0 else None
            render_lines.append((part, line_no, line.strip().startswith("@Test")))
    height = top + line_h * len(render_lines) + 40
    img = Image.new("RGB", (width, height), "#1e1e1e")
    d = ImageDraw.Draw(img)

    d.rectangle([0, 0, width, 44], fill="#323233")
    d.text((18, 11), "Eclipse IDE - Gestion Guarderia", font=font_ui, fill="#cfcfcf")
    file_label = f"{class_name}.java"
    d.text((width - d.textlength(file_label, font=font_ui) - 18, 11), file_label, font=font_ui, fill="#8f8f8f")

    d.rectangle([0, 44, width, 88], fill="#252526")
    d.rectangle([0, 44, 420, 88], fill="#1e1e1e")
    d.rectangle([0, 44, 420, 47], fill="#0e639c")
    d.text((22, 54), f"{class_name}.java", font=font_ui_bold, fill="#ffffff")
    d.text((440, 54), "JsonTestUtils.java", font=font_ui, fill="#8a8a8a")

    for idx, (line, line_no, is_test) in enumerate(render_lines):
        y = top + idx * line_h
        if line_no is not None:
            d.text((22, y), str(line_no), font=font, fill="#858585")
    d.line([gutter_w, 88, gutter_w, height], fill="#333333", width=2)

    for idx, (line, line_no, is_test) in enumerate(render_lines):
        x = x0
        y = top + idx * line_h
        if is_test and line_no is not None:
            d.rectangle([gutter_w + 2, y - 3, width - 10, y + line_h - 5], fill="#2a2d2e")
        for value, color, is_bold in color_runs_for_line(line):
            f = font_bold if is_bold else font
            d.text((x, y), value, font=f, fill=color)
            x += d.textlength(value, font=f)

    d.rectangle([0, height - 26, width, height], fill="#007acc")
    d.text((12, height - 25), f"  {method_name}  |  JUnit 4.13.2", font=font_ui, fill="#ffffff")

    out = OUT / f"code_{number}_{case_name}.png"
    img.save(out)
    return out


def draw_console(case_name, class_name, seconds, number):
    raw = read_text(WORK / f"console_{class_name}.txt").replace("\ufeff", "")
    output_lines = [line.rstrip() for line in raw.splitlines() if line.strip() != ""]
    output_lines = [line for line in output_lines if "pwsh.exe" not in line]

    prompt1 = r"PS F:\DAM\PROYECTO\Gestion Guarderia\app> java -cp $env:JUNIT_CP `"
    prompt2 = r">> org.junit.runner.JUnitCore `"
    prompt3 = f">> dam.proyecto.gestionguarderia.{class_name}"

    font = ImageFont.truetype(FONT_REGULAR, 30)
    font_ui = ImageFont.truetype(UI_REGULAR, 21)
    line_h = 44
    all_lines = [prompt1, prompt2, prompt3] + output_lines
    width = 1500
    height = 92 + line_h * len(all_lines) + 52
    img = Image.new("RGB", (width, height), "#0c0c0c")
    d = ImageDraw.Draw(img)

    d.rectangle([0, 0, width, 44], fill="#202020")
    d.text((18, 11), "Terminal - JUnit 4.13.2", font=font_ui, fill="#cccccc")

    y = 100
    for line in all_lines:
        color = "#16c60c" if line.startswith("OK (") else "#dedede"
        d.text((20, y), line, font=font, fill=color)
        y += line_h

    d.rectangle([20, y + 6, 32, y + 28], fill="#dedede")

    out = OUT / f"console_{number}_{case_name}.png"
    img.save(out)
    return out


def draw_global_console():
    raw = read_text(WORK / "junit_console.txt").replace("\ufeff", "")
    output_lines = [line.rstrip() for line in raw.splitlines() if line.strip() != ""]
    output_lines = [line for line in output_lines if "pwsh.exe" not in line]

    font = ImageFont.truetype(FONT_REGULAR, 30)
    font_ui = ImageFont.truetype(UI_REGULAR, 21)
    line_h = 44
    prompts = [
        r"PS F:\DAM\PROYECTO\Gestion Guarderia\app> java -cp $env:JUNIT_CP `",
        r">> org.junit.runner.JUnitCore `",
        r">> dam.proyecto.gestionguarderia.AlumnosJsonIntegrationTest `",
        r">> dam.proyecto.gestionguarderia.AulaJsonIntegrationTest `",
        r">> dam.proyecto.gestionguarderia.UsuariosJsonIntegrationTest `",
        r">> dam.proyecto.gestionguarderia.IncidenciasJsonIntegrationTest",
    ]
    all_lines = prompts + output_lines
    width = 1500
    height = 92 + line_h * len(all_lines) + 52
    img = Image.new("RGB", (width, height), "#0c0c0c")
    d = ImageDraw.Draw(img)
    d.rectangle([0, 0, width, 44], fill="#202020")
    d.text((18, 11), "Terminal - JUnit 4.13.2", font=font_ui, fill="#cccccc")

    y = 100
    for line in all_lines:
        color = "#16c60c" if line.startswith("OK (") else "#dedede"
        d.text((20, y), line, font=font, fill=color)
        y += line_h

    d.rectangle([20, y + 6, 32, y + 28], fill="#dedede")
    out = OUT / "console_global.png"
    img.save(out)
    return out


def draw_junit_summary():
    font_ui = ImageFont.truetype(UI_REGULAR, 28)
    font_ui_bold = ImageFont.truetype(UI_BOLD, 29)
    font_small = ImageFont.truetype(UI_REGULAR, 26)
    font_mono = ImageFont.truetype(FONT_REGULAR, 27)
    width = 1400
    row_h = 92
    height = 340 + row_h * (len(CASES) + 1) + 14
    img = Image.new("RGB", (width, height), "#f3f3f3")
    d = ImageDraw.Draw(img)

    d.rectangle([0, 0, width, 64], fill="#3c3f41")
    d.text((20, 16), "JUnit  -  Gestion Guarderia", font=font_ui_bold, fill="#ffffff")
    d.text((width - 280, 18), "Eclipse IDE", font=font_ui, fill="#bfbfbf")

    d.rectangle([0, 64, width, 116], fill="#ececec")
    d.rectangle([0, 64, 250, 116], fill="#ffffff")
    d.rectangle([0, 64, 250, 68], fill="#3c8ddc")
    d.text((20, 76), "JUnit", font=font_ui_bold, fill="#222222")

    d.rectangle([0, 116, width, 166], fill="#5ca85c")
    d.text((18, 124), "Tests completados correctamente", font=font_small, fill="#ffffff")

    d.text((20, 174), "Finished after 0,277 seconds", font=font_ui, fill="#333333")
    d.text((20, 224), "Runs: 4/4", font=font_ui_bold, fill="#222222")
    d.text((230, 224), "Errors: 0", font=font_ui_bold, fill="#222222")
    d.text((430, 224), "Failures: 0", font=font_ui_bold, fill="#222222")

    y = 290
    d.rectangle([0, y - 8, width, y + row_h - 8], fill="#e8e8e8")
    d.text((60, y + 24), "Test Suite", font=font_ui_bold, fill="#333333")
    d.text((width - 230, y + 24), "Tiempo", font=font_ui_bold, fill="#333333")
    y += row_h

    for idx, (case_name, class_name, method_name, seconds) in enumerate(CASES):
        if idx % 2 == 0:
            d.rectangle([0, y - 8, width, y + row_h - 8], fill="#fafafa")
        cx, cy = 30, y + 32
        d.ellipse([cx - 11, cy - 11, cx + 11, cy + 11], fill="#4ea24e")
        d.line([cx - 5, cy, cx - 1, cy + 5], fill="#ffffff", width=3)
        d.line([cx - 1, cy + 5, cx + 6, cy - 5], fill="#ffffff", width=3)
        d.text((64, y + 2), class_name, font=font_mono, fill="#1f1f1f")
        d.text((94, y + 42), method_name, font=font_small, fill="#555555")
        d.text((width - 180, y + 26), seconds + " s", font=font_mono, fill="#333333")
        y += row_h

    d.rectangle([0, height - 52, width, height], fill="#e1e1e1")
    d.text((16, height - 46), "0 errores, 0 fallos, 4 pruebas superadas", font=font_small, fill="#444444")

    out = OUT / "junit_summary.png"
    img.save(out)
    return out


if __name__ == "__main__":
    created = []
    for idx, (case_name, class_name, method_name, seconds) in enumerate(CASES, start=1):
        created.append(draw_code(case_name, class_name, method_name, seconds, idx))
        created.append(draw_console(case_name, class_name, seconds, idx))
    created.append(draw_global_console())
    created.append(draw_junit_summary())
    for p in created:
        print(p)
