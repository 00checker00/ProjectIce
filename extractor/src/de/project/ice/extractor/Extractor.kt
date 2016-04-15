package de.project.ice.extractor

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream
import java.util.*

fun main(args: Array<String>) {
    val lines: String
    if (args.size == 0) {
        val dir = File(".")
        val files = dir.listFiles { dir, name -> name.endsWith(".xlsx") }

        val sb = StringBuilder()
        for (xmlfile in files) {
            sb.append(extractLines(xmlfile.name))
            sb.append("\n")
        }
        lines=sb.toString()
    } else {
        val sb = StringBuilder()
        for (arg in args) {
            sb.append(extractLines(arg))
            sb.append("\n")
        }
        lines=sb.toString()
    }

    File("strings/ProjectIce.properties").writeText(lines)
    println("Wrote ${lines.lines().size} lines")
}

fun extractLines(name: String): String {
    val inp = FileInputStream(name);

    var result = HashMap<String, String>()

    val wb = WorkbookFactory.create(inp)
    result.putAll(extractLookups(wb.getSheet("Lookups")))
    result.putAll(extractDialogues(wb.getSheet("Dialog Conversation")))
    result.putAll(extractMessages(wb.getSheet("Messages")))

    val sb = StringBuilder()
    result.toList().sortedBy { it.first }.groupBy { it.first.substringBefore("_") }.forEach {
        sb.append("######## begin ${it.key} ########\n")
        it.value.forEach {
            sb.append("${it.first}=${it.second.replace("\num ", "\\n")}\n")
        }
        sb.append("######## end ${it.key} ########\n\n")
    }
    return sb.toString()
}

fun extractLookups(sheet: Sheet): Map<String, String> {
    val result = HashMap<String, String>(sheet.lastRowNum*2)
    for (row in sheet.rowIterator()) {
        val id = row.getCell(0)?.stringCellValue ?: continue
        val desc = row.getCell(1)?.stringCellValue
        val text = row.getCell(2)?.stringCellValue

        if (filter(row.getCell(0)))
            continue

        if (desc != null)
            result.put("${id}_desc", desc)
        if (text != null)
            result.put("${id}_text", text)
    }
    return result
}

fun extractDialogues(sheet: Sheet): Map<String, String> {
    val result = HashMap<String, String>(sheet.lastRowNum)
    for (row in sheet.rowIterator()) {
        val id = row.getCell(1)?.stringCellValue ?: continue
        val text = row.getCell(3)?.stringCellValue ?: continue

        if (filter(row.getCell(3)))
            continue

        result.put(id, text)
    }
    return result
}

fun extractMessages(sheet: Sheet): Map<String, String> {
    val result = HashMap<String, String>(sheet.lastRowNum)
    for (row in sheet.rowIterator()) {
        val id = row.getCell(0)?.stringCellValue ?: continue
        val text = row.getCell(1)?.stringCellValue ?: continue

        if (filter(row.getCell(0)))
            continue

        result.put(id, text)
    }
    return result
}

fun filter(cell: Cell): Boolean {
    if (cell.sheet.workbook.getFontAt(cell.cellStyle.fontIndex).bold)
        return true

    val text = cell.stringCellValue
    if (text.startsWith("<") && text.endsWith(">"))
        return true

    if (text.isEmpty())
        return true

    return false
}
