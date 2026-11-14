from pathlib import Path

from docx import Document
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.shared import Inches, Pt, RGBColor

BASE = Path(r"f:\DAM\PROYECTO\Gestion Guarderia")
IMAGES = BASE / "docs" / "images"
OUT_DOCX = BASE / "docs" / "Banco_Pruebas_Gestion_Guarderia.docx"

BODY_FONT = "Cambria"
MONO_FONT = "Consolas"


def set_font(run, name=BODY_FONT, size=10.5, bold=False, color=None, italic=False):
    run.font.name = name
    run.font.size = Pt(size)
    run.font.bold = bold
    run.font.italic = italic
    if color:
        run.font.color.rgb = RGBColor(*color)
    rpr = run._element.get_or_add_rPr()
    rfonts = rpr.find(qn("w:rFonts"))
    if rfonts is None:
        rfonts = OxmlElement("w:rFonts")
        rpr.append(rfonts)
    rfonts.set(qn("w:ascii"), name)
    rfonts.set(qn("w:hAnsi"), name)
    rfonts.set(qn("w:eastAsia"), name)


def heading(doc, text, size=12, before=10, after=5, level=1):
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(before)
    p.paragraph_format.space_after = Pt(after)
    p.paragraph_format.keep_with_next = True
    ppr = p._p.get_or_add_pPr()
    outline = OxmlElement("w:outlineLvl")
    outline.set(qn("w:val"), str(level))
    ppr.append(outline)
    set_font(p.add_run(text), size=size, bold=True, color=(0, 0, 0))
    return p


def body(doc, text, size=10.5, after=6, bold=False, italic=False, align=None):
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(after)
    p.paragraph_format.line_spacing = 1.12
    if align is not None:
        p.alignment = align
    set_font(p.add_run(text), size=size, bold=bold, italic=italic)
    return p


def no_borders(table):
    tbl_pr = table._tbl.tblPr
    borders = OxmlElement("w:tblBorders")
    for edge in ("top", "left", "bottom", "right", "insideH", "insideV"):
        el = OxmlElement("w:" + edge)
        el.set(qn("w:val"), "nil")
        borders.append(el)
    tbl_pr.append(borders)


def table_indent(table, inches=0.45):
    tbl_pr = table._tbl.tblPr
    ind = OxmlElement("w:tblInd")
    ind.set(qn("w:w"), str(int(inches * 1440)))
    ind.set(qn("w:type"), "dxa")
    tbl_pr.append(ind)


def cell_margins(table, top=70, bottom=70, left=70, right=70):
    for row in table.rows:
        for cell in row.cells:
            tc_pr = cell._tc.get_or_add_tcPr()
            mar = OxmlElement("w:tcMar")
            for side, value in (("top", top), ("left", left), ("bottom", bottom), ("right", right)):
                node = OxmlElement("w:" + side)
                node.set(qn("w:w"), str(value))
                node.set(qn("w:type"), "dxa")
                mar.append(node)
            tc_pr.append(mar)


def fill_cell(cell, value, bold=False):
    items = value if isinstance(value, list) else [value]
    first = True
    for item in items:
        p = cell.paragraphs[0] if first else cell.add_paragraph()
        first = False
        p.paragraph_format.space_before = Pt(0)
        p.paragraph_format.space_after = Pt(2)
        p.paragraph_format.line_spacing = 1.08
        if isinstance(value, list):
            p.paragraph_format.left_indent = Inches(0.16)
            p.paragraph_format.first_line_indent = Inches(-0.16)
            set_font(p.add_run("•  " + item), size=10, bold=bold)
        else:
            set_font(p.add_run(item), size=10, bold=bold)


def data_table(doc, rows):
    table = doc.add_table(rows=len(rows), cols=2)
    table.autofit = False
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table_indent(table)
    no_borders(table)
    for idx, (label, value) in enumerate(rows):
        fill_cell(table.cell(idx, 0), label)
        fill_cell(table.cell(idx, 1), value)
    table.columns[0].width = Inches(2.55)
    table.columns[1].width = Inches(3.55)
    for row in table.rows:
        row.cells[0].width = Inches(2.55)
        row.cells[1].width = Inches(3.55)
    cell_margins(table)
    return table


def add_image(doc, filename, width=6.55, keep_with_next=False):
    doc.add_picture(str(IMAGES / filename), width=Inches(width))
    p = doc.paragraphs[-1]
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.space_before = Pt(4)
    p.paragraph_format.space_after = Pt(10)
    p.paragraph_format.keep_with_next = keep_with_next


def caption(doc, text):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(10)
    set_font(p.add_run(text), size=8.5, italic=True, color=(90, 90, 90))


def configure_document(doc):
    sec = doc.sections[0]
    sec.page_width = Inches(8.27)
    sec.page_height = Inches(11.69)
    sec.left_margin = Inches(0.85)
    sec.right_margin = Inches(0.85)
    sec.top_margin = Inches(1.0)
    sec.bottom_margin = Inches(0.9)

    normal = doc.styles["Normal"]
    normal.font.name = BODY_FONT
    normal.font.size = Pt(10.5)
    normal.element.rPr.rFonts.set(qn("w:eastAsia"), BODY_FONT)
    normal.paragraph_format.space_after = Pt(6)
    normal.paragraph_format.line_spacing = 1.12


def add_test_section(doc, number, title, intro, rows, code_img, console_img, page_break_before=True):
    if page_break_before:
        doc.add_page_break()
    heading(doc, f"{number}. {title}", size=12, before=0 if page_break_before else 16, after=4)
    body(doc, intro, after=8)
    data_table(doc, rows)
    body(doc,
         "Evidencia de ejecución: en la página siguiente se incluye el código fuente del método "
         "@Test y la salida real de la consola, donde se observa el resultado OK (1 test).",
         size=9.5, italic=True, after=0)

    doc.add_page_break()
    add_image(doc, code_img, width=6.45, keep_with_next=True)
    caption(doc, "Código fuente del caso de prueba (captura real del IDE).")
    add_image(doc, console_img, width=6.45)
    caption(doc, "Salida real de la consola al ejecutar JUnit 4.13.2.")


def build_intro(doc):
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(18)
    set_font(p.add_run("BANCO DE PRUEBAS"), size=21, bold=True, color=(0, 0, 0))

    heading(doc, "PRUEBA DE INTEGRACIÓN", size=12.5, before=4, after=10)
    heading(doc, "1. Descripción", size=12, before=0, after=5)
    body(doc,
         "En este documento se detalla la especificación de los casos de prueba integrales "
         "diseñados y verificados para el proyecto Gestión Guardería (GuarderíaApp). Estas "
         "pruebas tienen como objetivo validar la lectura de los mocks JSON almacenados en "
         "assets, el mapeo de dichos datos a las entidades de respuesta mediante Gson y la "
         "integridad referencial entre alumnos, aulas, usuarios e incidencias.",
         after=9)
    data_table(doc, [
        ("Framework de pruebas", "JUnit 4.13.2 (Java 11)"),
        ("Herramienta de construcción", "Gradle 9.2.1 + Android Gradle Plugin 9.0.1"),
        ("Librería de serialización", "Gson 2.10.1"),
        ("Fuente de datos", "Assets JSON del proyecto: alumnos.json, aula.json, usuarios.json e incidencias.json"),
        ("Mecanismo de ejecución", "JUnitCore sobre la variante debugUnitTest, sin emulador ni dispositivo"),
    ])
    body(doc,
         "Cada instancia se ejecuta de forma aislada y comprueba tanto el contenido recuperado "
         "como las relaciones declaradas entre los identificadores de los mocks. La ejecución "
         "global se realizó con el comando gradlew :app:testDebugUnitTest y se complementó con "
         "JUnitCore para obtener la salida de consola que se muestra en cada sección.",
         after=4)

    heading(doc, "Contenido del documento", size=11, before=8, after=4)
    for item in (
        "1. Descripción y contexto de ejecución.",
        "2. Primera Instancia (Mock de Alumno).",
        "3. Segunda Instancia (Mock de Aula).",
        "4. Tercera Instancia (Mock de Usuario).",
        "5. Cuarta Instancia (Mock de Incidencia).",
        "6. Resultados globales y conclusión.",
    ):
        body(doc, item, size=10, after=2)

    heading(doc, "Alcance de la verificación", size=11, before=8, after=4)
    for item in (
        "• Lectura de los cuatro assets JSON almacenados en app/src/main/assets.",
        "• Mapeo de los registros a las entidades de respuesta mediante Gson 2.10.1.",
        "• Integridad referencial entre alumnos, aulas, usuarios e incidencias.",
        "• Evidencia de ejecución con JUnit 4.13.2 y salida real de consola.",
    ):
        body(doc, item, size=10, after=2)


def build_alumnos(doc):
    add_test_section(
        doc, 2, "Primera Instancia (Mock de Alumno)",
        "Se carga un array de objetos de tipo Alumno desde el mock para validar que Gson "
        "reconstruye correctamente los registros, que los apellidos y la fecha de nacimiento "
        "se conservan y que la relación entre Alumno y Aula es coherente con el repositorio "
        "mock de aulas.",
        [
            ("Archivo de Test", "AlumnosJsonIntegrationTest.java"),
            ("Descripción",
             "Validar la lectura de assets/alumnos.json, el mapeo con Gson a AlumnosResponse "
             "y la integridad referencial entre cada alumno, su aula y su tutor legal."),
            ("Condiciones de ejecución", [
                "Assets del proyecto disponibles en app/src/main/assets",
                "Classpath de test con JUnit 4.13.2 y Gson 2.10.1",
                "Ejecución de gradlew :app:testDebugUnitTest",
            ]),
            ("Datos de entrada", [
                "Alumno 1: nombre = 'Alvaro', apellidos = 'Pulido Torres', fechaNacimiento = '15/03/2025'",
                "Alumno 2: nombre = 'Cristina'",
                "aulaCodigo y tutorlegalCodigo declarados en cada registro del mock",
            ]),
            ("Resultado esperado y aserciones", [
                "El mock contiene exactamente 2 alumnos.",
                "Gson crea el array AlumnosResponse[] sin valores nulos.",
                "Los campos nombre, apellidos y fechaNacimiento coinciden con el JSON.",
                "Cada aulaCodigo del alumno existe en aula.json (integridad referencial).",
                "Cada tutorlegalCodigo es un identificador positivo.",
                "Resultado global del test: OK (1 test).",
            ]),
        ],
        "code_1_Alumnos.png", "console_1_Alumnos.png")


def build_aula(doc):
    add_test_section(
        doc, 3, "Segunda Instancia (Mock de Aula)",
        "Se instancia el repositorio mock de Aula para valorar que la consulta devuelve "
        "resultados y que los nombres de aula y del profesor recuperados coinciden con los "
        "datos insertados en el JSON.",
        [
            ("Archivo de Test", "AulaJsonIntegrationTest.java"),
            ("Descripción",
             "Verificar la lectura de assets/aula.json, el mapeo de los campos nombreAula, "
             "profesorNombre e idProfesor y la coherencia entre el aula y su profesor."),
            ("Condiciones de ejecución", [
                "Assets del proyecto disponibles en app/src/main/assets",
                "Classpath de test con JUnit 4.13.2 y Gson 2.10.1",
                "Ejecución de gradlew :app:testDebugUnitTest",
            ]),
            ("Datos de entrada", [
                "Aula 1: nombreAula = 'Perro', profesorNombre = 'Lucia Jimenez Marin', idProfesor = 6",
                "Aula 2: nombreAula = 'Gato', idProfesor = 7",
            ]),
            ("Resultado esperado y aserciones", [
                "Gson crea el array AulaResponse[] sin valores nulos.",
                "El mock contiene exactamente 2 aulas.",
                "El aula 'Perro' conserva su profesor y su identificador 6.",
                "El aula 'Gato' conserva su identificador de profesor 7.",
                "Resultado global del test: OK (1 test).",
            ]),
        ],
        "code_2_Aula.png", "console_2_Aula.png")


def build_usuarios(doc):
    add_test_section(
        doc, 4, "Tercera Instancia (Mock de Usuario)",
        "Se instancia un objeto de tipo Usuario para comprobar que el rol se mapea como "
        "enumerado, que el administrador conserva sus credenciales de ejemplo y que la "
        "distribución de perfiles del sistema de acceso es la esperada.",
        [
            ("Archivo de Test", "UsuariosJsonIntegrationTest.java"),
            ("Descripción",
             "Validar la lectura de assets/usuarios.json, el mapeo del rol como enumerado "
             "UsuariosResponse.rol y la distribución de usuarios por perfil."),
            ("Condiciones de ejecución", [
                "Assets del proyecto disponibles en app/src/main/assets",
                "Classpath de test con JUnit 4.13.2 y Gson 2.10.1",
                "Ejecución de gradlew :app:testDebugUnitTest",
            ]),
            ("Datos de entrada", [
                "Usuario administrador: idUsuario = 1, correoElectronico = 'pepgarlop@ejemplo.com', rol = 'ADMINISTRADOR'",
                "Roles declarados: ADMINISTRADOR, ADMINISTRACION, TUTOR LEGAL y PROFESOR",
                "Total de usuarios del mock: 7",
            ]),
            ("Resultado esperado y aserciones", [
                "El mock contiene exactamente 7 usuarios.",
                "Gson crea el usuario administrador sin valores nulos.",
                "El identificador y el correo del administrador coinciden con el JSON.",
                "El rol del primer registro es igual a UsuariosResponse.rol.ADMINISTRADOR.name().",
                "Distribución esperada: 1 ADMINISTRADOR, 2 ADMINISTRACION, 2 TUTOR LEGAL y 2 PROFESOR.",
                "Resultado global del test: OK (1 test).",
            ]),
        ],
        "code_3_Usuarios.png", "console_3_Usuarios.png")


def build_incidencias(doc):
    add_test_section(
        doc, 5, "Cuarta Instancia (Mock de Incidencia)",
        "Se instancia un objeto del tipo Incidencia para validar la correcta creación del "
        "objeto, el contenido recuperado y que el código de registro diario asociado no se "
        "repite entre incidencias.",
        [
            ("Archivo de Test", "IncidenciasJsonIntegrationTest.java"),
            ("Descripción",
             "Validar la lectura de assets/incidencias.json, el mapeo de la entidad "
             "IncidenciasResponse y la unicidad de los códigos de registro diario."),
            ("Condiciones de ejecución", [
                "Assets del proyecto disponibles en app/src/main/assets",
                "Classpath de test con JUnit 4.13.2 y Gson 2.10.1",
                "Ejecución de gradlew :app:testDebugUnitTest",
            ]),
            ("Datos de entrada", [
                "Incidencia 1: incidencia = 'Caida en el recreo', registroCodigo = 1",
                "Incidencia 2: incidencia = 'Caca en clase', registroCodigo = 2",
            ]),
            ("Resultado esperado y aserciones", [
                "Gson crea el array IncidenciasResponse[] sin valores nulos.",
                "El mock contiene exactamente 2 incidencias.",
                "El texto y el registroCodigo de cada incidencia coinciden con el JSON.",
                "Los códigos de registro diario son distintos entre sí.",
                "Resultado global del test: OK (1 test).",
            ]),
        ],
        "code_4_Incidencias.png", "console_4_Incidencias.png")


def build_results(doc):
    doc.add_page_break()
    heading(doc, "6. Resultados globales", size=12, before=0, after=6)
    body(doc,
         "Todas las pruebas de integración se ejecutaron de manera satisfactoria, indicando "
         "una correcta lectura de los assets JSON, un mapeo estable con Gson y una adecuada "
         "integridad referencial entre las entidades del proyecto. La ejecución conjunta de "
         "las cuatro suites finalizó sin errores ni fallos.",
         after=8)
    data_table(doc, [
        ("Casos ejecutados", "4 suite de prueba / 4 métodos @Test"),
        ("Resultado", "OK (4 tests)"),
        ("Errores", "0"),
        ("Fallos", "0"),
        ("Tiempo total observado", "0,277 s (suma de los tiempos individuales)"),
    ])
    body(doc,
         "La siguiente captura resume la ejecución completa en el runner de JUnit: cuatro "
         "pruebas superadas, ningún error y ningún fallo.",
         after=6)
    add_image(doc, "junit_summary.png", width=6.55)
    caption(doc, "Resumen de la ejecución de las cuatro suites en JUnit 4.13.2.")

    doc.add_page_break()
    body(doc,
         "La salida de consola de la ejecución conjunta confirma el resultado global del "
         "banco de pruebas y permite trazabilidad directa con los casos descritos.",
         after=6)
    add_image(doc, "console_global.png", width=6.55)
    caption(doc, "Consola global: OK (4 tests) sobre las cuatro clases de integración.")

    body(doc,
         "Conclusión: los cuatro casos de integración validan de extremo a extremo los mocks "
         "JSON utilizados por la aplicación. La comprobación de campos, roles, identificadores "
         "y relaciones entre alumnos y aulas aporta evidencia suficiente sobre la consistencia "
         "de los datos que consume Gestión Guardería.",
         after=4)


def main():
    doc = Document()
    configure_document(doc)
    build_intro(doc)
    build_alumnos(doc)
    build_aula(doc)
    build_usuarios(doc)
    build_incidencias(doc)
    build_results(doc)
    doc.save(OUT_DOCX)
    print(OUT_DOCX)


if __name__ == "__main__":
    main()





