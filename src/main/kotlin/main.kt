import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.gson.Gson
import model.Region
import model.Town
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.PrintWriter

fun main() {

  val enCsvFile = "Myanmar_Locations_Postal_Code_EN.csv"
  val mmCsvFile = "Myanmar_Locations_Postal_Code_MM.csv"

  val enRegionTownMap = mutableMapOf<String, List<String>>()
  val mmRegionTownMap = mutableMapOf<String, List<String>>()

  //en csv read
  csvReader().open(enCsvFile) {
    val all = readAllAsSequence().toList().toMutableList()

    all.removeFirst()

    all.groupBy { item ->
      //firstly group by with region
      item[0]
    }.forEach { (r, u) ->

      //  println("region name >> $r : ")

      val townList = mutableListOf<String>()
      //secondly group by with town township
      u.groupBy { item ->
        item[1]
      }.forEach { (t, u) ->
        if (t.trim() != "-") {
          // println("\ttown/township name >> $t")
          townList.add(t)
        }

      }

      enRegionTownMap[r] = townList

    }

  }

  //mm csv read
  csvReader().open(mmCsvFile) {
    val all = readAllAsSequence().toList().toMutableList()

    all.removeFirst()

    all.groupBy { item ->
      //firstly group by with region
      item[0]
    }.forEach { (r, u) ->

      // println("region name >> $r : ")

      val townList = mutableListOf<String>()
      //secondly group by with town township
      u.groupBy { item ->
        item[1]
      }.forEach { (t, u) ->
        if (t.trim() != "-") {
          // println("\ttown/township name >> $t")
          townList.add(t)
        }
      }

      mmRegionTownMap[r] = townList

    }
  }

  println(
    "en total region >> ${enRegionTownMap.entries.size} and mm total region >> ${mmRegionTownMap.entries.size}"
  )

  val regionJsonPath = "json/regions.json"
  val townJsonPath = "json/towns.json"

  var townCounter = 0

  val regionList = mutableListOf<Region>()
  val townList = mutableListOf<Town>()

  for (i in 0 until enRegionTownMap.size) {

    val regionId = i + 1

    val regionNameEn = enRegionTownMap.keys.elementAt(i)
    val regionNameMm = mmRegionTownMap.keys.elementAt(i)

    val region =
      Region(region_id = regionId, region_name_en = regionNameEn, region_name_mm = regionNameMm)

    regionList.add(region)



    for (j in enRegionTownMap[regionNameEn]!!.indices) {
      townCounter++

      val townId = townCounter

      val townNameEn = enRegionTownMap[regionNameEn]!![j]
      val townNameMm = mmRegionTownMap[regionNameMm]!![j]

      val town = Town(
        town_id = townId,
        town_name_en = townNameEn,
        town_name_mm = townNameMm,
        region_id = regionId
      )

      townList.add(town)
    }
  }

  writeRegion(region = regionList, path = regionJsonPath)
  writeTown(town = townList, path = townJsonPath)
}

fun writeRegion(region: List<Region>, path: String) {

  try {

    val file = File(path)

    if (File(path).exists().not())
      file.createNewFile()

    FileOutputStream(file, true).bufferedWriter().use {
      val gson = Gson()
      val jsonString = gson.toJson(region)
      it.append(jsonString)
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }
}

fun writeTown(town: List<Town>, path: String) {
  try {

    val file = File(path)

    if (File(path).exists().not())
      file.createNewFile()

    FileOutputStream(file, true).bufferedWriter().use {
      val gson = Gson()
      val jsonString = gson.toJson(town)
      it.append(jsonString)
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }
}