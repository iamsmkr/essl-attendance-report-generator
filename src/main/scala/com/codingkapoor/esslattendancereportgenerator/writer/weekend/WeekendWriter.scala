package com.codingkapoor.esslattendancereportgenerator.writer.weekend

import java.time.{DayOfWeek, LocalDate, YearMonth}

import com.codingkapoor.esslattendancereportgenerator.model.{AttendancePerEmployee, Holiday}
import com.codingkapoor.esslattendancereportgenerator.writer.AttendanceDimensions
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook

trait WeekendWriter extends WeekendStyle {
  val month: Int
  val year: Int

  def mergedRegionAlreadyExists(firstRowIndex: Int, lastRowIndex: Int, firstColumnIndex: Int, lastColumnIndex: Int)(implicit workbook: XSSFWorkbook): Boolean

  def writeWeekends(implicit workbook: XSSFWorkbook, attendances: Seq[AttendancePerEmployee], holidays: Seq[Holiday]): Unit = {
    val yearMonth = YearMonth.of(year, month)
    val _month = yearMonth.getMonth.toString

    val sheet = workbook.getSheet(_month)

    val attendanceDimensions = AttendanceDimensions(month, year, attendances.map(l => l.employee))

    val mergedRegionFirstRowIndex = attendanceDimensions.firstRowIndex
    val mergedRegionLastRowIndex = attendanceDimensions.lastRowIndex - 1

    val (satCellStyle, sunCellStyle) = getWeekendCellStyle

    for (dayOfMonth <- 1 to yearMonth.lengthOfMonth()) {
      val dayOfWeek = LocalDate.of(year, month, dayOfMonth).getDayOfWeek
      if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
        val dayIndex = attendanceDimensions.firstColumnIndex + dayOfMonth - 1

        val mergedRegionFirstColumnIndex = dayIndex
        val mergedRegionLastColumnIndex = dayIndex

        if (!mergedRegionAlreadyExists(mergedRegionFirstRowIndex, mergedRegionLastRowIndex, mergedRegionFirstColumnIndex, mergedRegionLastColumnIndex)) {
          for (i <- mergedRegionFirstRowIndex to mergedRegionLastRowIndex) {
            val row = sheet.getRow(i)
            for (j <- mergedRegionFirstColumnIndex to mergedRegionLastColumnIndex) {
              val col = row.createCell(j)

              if (dayOfWeek == DayOfWeek.SATURDAY) col.setCellStyle(satCellStyle)
              else col.setCellStyle(sunCellStyle)

              if (i == mergedRegionFirstRowIndex && j == mergedRegionFirstColumnIndex) {
                val dayOfWeekStr = dayOfWeek.toString
                val camelCasedDayOfWeek = dayOfWeekStr.take(1) + dayOfWeekStr.drop(1).toLowerCase
                col.setCellValue(camelCasedDayOfWeek)
              }
            }
          }

          sheet.addMergedRegion(new CellRangeAddress(mergedRegionFirstRowIndex, mergedRegionLastRowIndex, mergedRegionFirstColumnIndex, mergedRegionLastColumnIndex))
        }
      }
    }
  }
}
