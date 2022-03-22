package com.clearcover.forms.helpers

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.BufferedInputStream
import java.io.FileInputStream


class PdfHelper {
    companion object {
        fun readFile(fileName: String): String {
            val fileStream = FileInputStream(fileName)
            val bf = BufferedInputStream(fileStream)
            val doc: PDDocument = PDDocument.load(bf)
            val content = PDFTextStripper().getText(doc)
            fileStream.close()
            bf.close()
            doc.close()
            return content
        }
    }
}