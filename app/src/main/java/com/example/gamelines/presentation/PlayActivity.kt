package com.example.gamelines.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.*
import android.widget.*
import com.example.gamelines.R
import com.example.gamelines.utils.InversedMapOfColors
import com.example.gamelines.utils.MapOfColors
import com.example.gamelines.domain.objects.Table
import com.example.gamelines.databinding.ActivityMainBinding

const val NEW_BALLS = 3 //how many new balls will appear
const val MIN_BALLS_TO_DESTROY = 5 //minimal number of balls in line to destroy
val TABLESIZE = 9//size of game field
val NUMBER_OF_COLORS = 7 //how many colors of balls available
var scoreSTR = "0"//current score
var highScoreSTR = "0"//high score
var threeBalls = Array(NEW_BALLS) { 0 }//colors of random balls under table
var isFutureBalls = true //does buttonShow show random balls
var table: Table = Table()//table of the game field

class PlayActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.Block.visibility = VISIBLE
        binding.Block.bringToFront()
    }

    fun startGame(view: View) {
        RandomBallsCreation().randColor()
        PointsConvertation().clearPoints()
        ChangingGameTable().clearTable()
        view.visibility = GONE
        binding.Block.visibility = GONE
        binding.Score.setText(scoreSTR)
        binding.buttonShow.visibility = VISIBLE
        for (x in 0..TABLESIZE - 1)
            for (y in 0..TABLESIZE - 1) {
                val tabRow = binding.tableLayout.getChildAt(x) as TableRow
                val cell = tabRow.getChildAt(y) as ImageView
                cell.isClickable = true
            }
        RandomBallsCreation().randomCellBall()
        drawRand()
        showTable()
    }

    fun game() {
        RandomBallsCreation().randomCellBall()
        showTable()
        showPoints()
        var emptyCells = 0
        for (x in 0..TABLESIZE - 1) for (y in 0..TABLESIZE - 1) if (table.isEquals(
                x,
                y,
                0
            )
        ) emptyCells++
        if (emptyCells > 0) {
            drawRand()
        } else {
            gameOver()
        }
    }

    fun gameOver() {
        val intent: Intent = Intent(this@PlayActivity, EndgameActivity::class.java)
        intent.putExtra(getString(R.string.current), scoreSTR)
        intent.putExtra(getString(R.string.high), highScoreSTR)
        startActivity(intent)
        /*binding.Prize.visibility = VISIBLE
        binding.Block2.bringToFront()
        binding.Yours.bringToFront()
        binding.TotalScore.bringToFront()
        binding.Nickname.bringToFront()
        binding.Best.bringToFront()
        binding.TotalBestScore.bringToFront()
        binding.ButtonPublic.bringToFront()
        binding.ButtonRestart.bringToFront()
        binding.TotalScore.setText(scoreSTR)
        binding.TotalBestScore.setText(highScoreSTR)*/
        //binding.buttonShow.visibility = INVISIBLE
    }

    fun publish(view: View) {
        Toast.makeText(applicationContext, "в разработке", Toast.LENGTH_LONG).show()
    }

    fun findByIdString(text: String): IntArray {
        val arr = IntArray(2)
        arr[0] = text[text.length - 2].toInt() - '0'.toInt()
        arr[1] = text[text.length - 1].toInt() - '0'.toInt()
        return arr
    }

    fun showPoints() {
        binding.Score.setText(scoreSTR)
        binding.HighScore.setText(highScoreSTR)
    }

    fun showTable() {
        for (x in 0..TABLESIZE - 1)
            for (y in 0..TABLESIZE - 1) {
                val tabRow = binding.tableLayout.getChildAt(x) as TableRow
                val cell = tabRow.getChildAt(y) as ImageView
                cell.setImageDrawable(MapOfColors().getColor(applicationContext, table.getValue(x,y)))
            }
        showPoints()
    }

    fun isShown(): Boolean {
        if (binding.bubble1.visibility == VISIBLE) return true else return false
    }

    fun showB() {
        binding.textNext.visibility = INVISIBLE
        binding.bubble1.visibility = INVISIBLE
        binding.bubble2.visibility = INVISIBLE
        binding.bubble3.visibility = INVISIBLE
        val s = """Показать будущие
            |шарики""".trimMargin()
        binding.buttonShow.setText(s)
        isFutureBalls = false
    }

    fun unshowB() {
        binding.textNext.visibility = VISIBLE
        binding.bubble1.visibility = VISIBLE
        binding.bubble2.visibility = VISIBLE
        binding.bubble3.visibility = VISIBLE
        val s = """Скрыть будущие
            |шарики""".trimMargin()
        binding.buttonShow.setText(s)
        isFutureBalls = true
    }

    fun showBubbles(view: View) {
        if (isShown()) showB() else unshowB()
    }

    fun drawRand() {
        binding.bubble1.setImageDrawable(MapOfColors().getColor(applicationContext, threeBalls[0]))
        binding.bubble2.setImageDrawable(MapOfColors().getColor(applicationContext, threeBalls[1]))
        binding.bubble3.setImageDrawable(MapOfColors().getColor(applicationContext, threeBalls[2]))
    }

    //Block of selection current ball and for his moving
    var isYellowBlock: Boolean = false //existence of selected ball
    var yellowCell = IntArray(2) //coordinates of selected ball
    var yellowType = 0 //type of selected ball, empty by default
    fun yellowBack(view: View) {
        val image = view as ImageView
        if (isYellowBlock == true) {
            if (image.drawable.constantState == getDrawable(R.drawable.empty)!!.constantState) {
                if (PathCalculation().pathAccess(
                        yellowCell,
                        findByIdString(image.getTag().toString())
                    )
                ) {
                    ChangingGameTable().paintEmpty(
                        yellowCell,
                        yellowType,
                        findByIdString(image.getTag().toString())
                    )
                    isYellowBlock = false
                    yellowType = 0
                    val sum =
                        BallsDestroying().checkLines(findByIdString(image.getTag().toString()))
                    showTable()
                    PointsConvertation().addPoints(sum)
                    showPoints()
                    if (sum == 0) game()
                }
                //for case "no way for ball"
                else {
                    val timer = object : CountDownTimer(300, 1000) {
                        override fun onTick(p0: Long) {
                            image.setImageDrawable(getDrawable(R.drawable.wrong))
                        }

                        override fun onFinish() {
                            image.setImageDrawable(getDrawable(R.drawable.empty))
                        }
                    }
                    timer.start()
                }
            }
            //for case "2-d choosing ball"
            else {
                if (image.drawable.constantState != getDrawable(R.drawable.wrong)!!.constantState) {
                    val tabRow = binding.tableLayout.getChildAt(yellowCell[0]) as TableRow
                    val cell = tabRow.getChildAt(yellowCell[1]) as ImageView //yellow cell
                    if (cell.getTag() == image.getTag()) dropColor(image)
                    else paintBall(yellowCell, yellowType, image)
                }
            }
        }
        //for case "1-st choosing ball"
        else {
            if (image.drawable.constantState != getDrawable(R.drawable.empty)!!.constantState) {
                newColor(image)
                yellowCell = findByIdString(view.getTag().toString())
                isYellowBlock = true
            }
        }
    }

    fun paintBall(
        coords: IntArray,
        color: Int,
        selected: ImageView
    ) {//changing selected ball, "selected" - new cell
        val tabRow = binding.tableLayout.getChildAt(coords[0]) as TableRow
        val cell = tabRow.getChildAt(coords[1]) as ImageView //selected ball
        cell.setImageDrawable(MapOfColors().getColor(applicationContext, color))//delete selection of a ball
        changeColor(selected)
        yellowCell =
            findByIdString(selected.getTag().toString())//changing coordinates of selected ball
    }

    fun newColor(view: ImageView) {
        changeColor(view)
        isYellowBlock = true
    }

    fun dropColor(view: ImageView) {
        /*for (x in inversedMapOfColors) if (view.drawable.constantState == x.key) {
            view.setImageDrawable(getDrawable(colors[x.value - 10]!!))
        }*/
        var currentInt = InversedMapOfColors(applicationContext).getCurrentInt(view.drawable.constantState!!)
        if (currentInt in 11..17) currentInt-=10
        view.setImageDrawable(MapOfColors().getColor(applicationContext, currentInt))

        /*when (view.drawable.constantState) {
            getDrawable(R.drawable.redbubble_y)!!.constantState -> view.setImageDrawable(
                getDrawable(
                    R.drawable.redbubble
                )
            )
            getDrawable(R.drawable.greenbubble_y)!!.constantState -> view.setImageDrawable(
                getDrawable(R.drawable.greenbubble)
            )
            getDrawable(R.drawable.bluebubble_y)!!.constantState -> view.setImageDrawable(
                getDrawable(R.drawable.bluebubble)
            )
            getDrawable(R.drawable.orangebubble_y)!!.constantState -> view.setImageDrawable(
                getDrawable(R.drawable.orangebubble)
            )
            getDrawable(R.drawable.purplebubble_y)!!.constantState -> view.setImageDrawable(
                getDrawable(R.drawable.purplebubble)
            )
            getDrawable(R.drawable.brownbubble_y)!!.constantState -> view.setImageDrawable(
                getDrawable(R.drawable.brownbubble)
            )
            getDrawable(R.drawable.skybubble_y)!!.constantState -> view.setImageDrawable(
                getDrawable(
                    R.drawable.skybubble
                )
            )
        }*/
        yellowType = 0
        isYellowBlock = false
    }

    //ball's selection
    fun changeColor(view: ImageView) {
        /*for (x in inversedMapOfColors) if (view.drawable.constantState == x.key) {
            view.setImageDrawable(getDrawable(colors[x.value + 10]!!))
            yellowType = x.value
            break
        }*/
        val currentInt = InversedMapOfColors(applicationContext).getCurrentInt(view.drawable.constantState!!)
        if (currentInt in 1..7) yellowType = currentInt-10 else yellowType = 0
        view.setImageDrawable(MapOfColors().getColor(applicationContext, currentInt+10))

        /*when (view.drawable.constantState) {
            getDrawable(R.drawable.redbubble)!!.constantState -> {
                view.setImageDrawable(getDrawable(R.drawable.redbubble_y)); yellowType = 1
            }
            getDrawable(R.drawable.greenbubble)!!.constantState -> {
                view.setImageDrawable(getDrawable(R.drawable.greenbubble_y)); yellowType = 2
            }
            getDrawable(R.drawable.bluebubble)!!.constantState -> {
                view.setImageDrawable(getDrawable(R.drawable.bluebubble_y)); yellowType = 3
            }
            getDrawable(R.drawable.orangebubble)!!.constantState -> {
                view.setImageDrawable(getDrawable(R.drawable.orangebubble_y)); yellowType = 4
            }
            getDrawable(R.drawable.purplebubble)!!.constantState -> {
                view.setImageDrawable(getDrawable(R.drawable.purplebubble_y)); yellowType = 5
            }
            getDrawable(R.drawable.brownbubble)!!.constantState -> {
                view.setImageDrawable(getDrawable(R.drawable.brownbubble_y)); yellowType = 6
            }
            getDrawable(R.drawable.skybubble)!!.constantState -> {
                view.setImageDrawable(getDrawable(R.drawable.skybubble_y)); yellowType = 7
            }
        }*/
    }
}

class ChangingGameTable {
    fun clearTable() {
        for (x in 0..TABLESIZE - 1)
            for (y in 0..TABLESIZE - 1)
                table.setValue(x, y, 0)
    }

    fun paintEmpty(coords: IntArray, color: Int, selected: IntArray) {//moving ball into empty cell
        table.setValue(selected[0], selected[1], color)
        table.setValue(coords[0], coords[1], 0)
    }
}

//Lee's algorithm (Wave Trace Algorithm)
class PathCalculation {
    fun pathAccess(
        yellowCoords: IntArray,
        coords: IntArray
    ): Boolean { //checking that the ball can move from the starting position to the selected empty cell
        val pathTable = Table()
        for (x in 0..TABLESIZE - 1)
            for (y in 0..TABLESIZE - 1) {
                if ((table.isEquals(x, y, 0)).not()) pathTable.setValue(x, y, -1)
            }
        pathTable.setValue(yellowCoords[0], yellowCoords[1], 1)
        var cnt = 1
        while (pathTable.isEquals(coords[0], coords[1], 0)) {
            path(pathTable, cnt)
            cnt++
            if (cnt >= TABLESIZE * TABLESIZE) break
        }
        if ((pathTable.isEquals(coords[0], coords[1], 0)).not()) return true
        else return false
    }

    fun path(mainTable: Table, num: Int) {
        var count = 0
        for (x in 0..TABLESIZE - 1) for (y in 0..TABLESIZE - 1) if (mainTable.isEquals(
                x,
                y,
                num
            )
        ) count += 2
        if (count != 1) {
            val arr = IntArray(count)
            var n = 0
            for (x in 0..TABLESIZE - 1)
                for (y in 0..TABLESIZE - 1) if (mainTable.isEquals(x, y, num)) {
                    arr[n] = x
                    n++
                    arr[n] = y
                    n++
                }
            for (i in 0..n - 1 step 2) {
                val x = arr[i];
                val y = arr[i + 1]
                if (x - 1 >= 0) if (mainTable.isEquals(x - 1, y, 0)) mainTable.setValue(
                    x - 1,
                    y,
                    num + 1
                )
                if (y - 1 >= 0) if (mainTable.isEquals(x, y - 1, 0)) mainTable.setValue(
                    x,
                    y - 1,
                    num + 1
                )
                if (x + 1 <= TABLESIZE - 1) if (mainTable.isEquals(x + 1, y, 0)) mainTable.setValue(
                    x + 1,
                    y,
                    num + 1
                )
                if (y + 1 <= TABLESIZE - 1) if (mainTable.isEquals(x, y + 1, 0)) mainTable.setValue(
                    x,
                    y + 1,
                    num + 1
                )
            }
        }
    }
}

class RandomBallsCreation {
    fun randomCellBall() {
        var k = 0
        while (k < NEW_BALLS) { //NEW_BALLS = 3 by default
            val x = (0..TABLESIZE - 1).random()
            val y = (0..TABLESIZE - 1).random()
            if (table.isEquals(x, y, 0)) {
                table.setValue(x, y, threeBalls[k])
                val sum = BallsDestroying().checkLines(intArrayOf(x, y))
                if (sum > 0) {
                    PointsConvertation().addPoints(sum)
                }
                var free = 0
                for (i in 0..TABLESIZE - 1)
                    for (j in 0..TABLESIZE - 1)
                        if (table.isEquals(i, j, 0)) free += 1
                if (free != 0) k++ else break
            }
        }
        randColor()
    }

    fun randColor() {
        threeBalls = Array(3, { (1..NUMBER_OF_COLORS).random() })
    }
}

class PointsConvertation {
    fun tableOfPoints(balls: Int): Int {
        when (balls) {
            in 0..4 -> return 0
            5 -> return 10
            6 -> return 15
            7 -> return 22
            8 -> return 30
            9 -> return 40
            10 -> return 52
            11 -> return 65
            12 -> return 80
            13 -> return 100
            14 -> return 135
            15 -> return 180
            16 -> return 250
            17 -> return 350
            18 -> return 500
            19 -> return 800
            20 -> return 1_200
            21 -> return 2_000
            22 -> return 3_000
            23 -> return 5_000
            24 -> return 8_000
            25 -> return 12_000
            26 -> return 17_000
            27 -> return 25_000
            28 -> return 35_000
            29 -> return 50_000
            30 -> return 80_000
            31 -> return 120_000
            32 -> return 200_000
            33 -> return 300_000
            34 -> return 500_000
            else -> return 1_000_000
        }
    }

    fun addPoints(point: Int) {
        scoreSTR = (scoreSTR.toInt() + tableOfPoints(point)).toString()
        if (highScoreSTR.toInt() < scoreSTR.toInt()) highScoreSTR = scoreSTR
    }

    fun clearPoints() {
        scoreSTR = "0"
    }
}

class BallsDestroying {
    fun checkLines(X: IntArray): Int {
        var sum = 0
        val x = X[0]
        val y = X[1]
        val color = table.getValue(x, y)
        val destroyArr: Array<Array<Int>> = Array(4, { Array(3, { -1 }) })
        destroyArr[0] = checkHoriz(x, color)
        destroyArr[1] = checkVert(y, color)
        destroyArr[2] = checkDiagOne(x, y, color)
        destroyArr[3] = checkDiagTwo(x, y, color)
        for (k in 0..3)
            if (sum == 0) sum += destroyArr[k][2]
            else if (destroyArr[k][2] != 0) sum += destroyArr[k][2] - 1
        if (sum != 0) destroyBalls(destroyArr)
        return sum
    }

    //Block of checking in 4 directions
    fun checkHoriz(
        x: Int,
        c: Int
    ): Array<Int> {//at the input, the coordinates of the cell from which the counting starts and its color; at the output, the coordinates of the beginning of the line and its length
        val arr =
            Array(3) { -1 } //first - row number, second - column number, third - number of balls in the line
        arr[2] = 0
        for (k in 0..TABLESIZE - 1) if (table.isEquals(x, k, c)) {
            arr[2]++
            if (arr[1] == -1) arr[1] = k
        } else if (arr[2] >= MIN_BALLS_TO_DESTROY) break
        else {
            arr[2] = 0; arr[1] = -1
        }

        if (arr[2] < MIN_BALLS_TO_DESTROY) {
            arr[1] = -1; arr[2] = 0
        }
        if (arr[2] != 0) arr[0] = x
        return arr
    }

    fun checkVert(y: Int, c: Int): Array<Int> {
        val arr =
            Array(3) { -1 } //first - row number, second - column number, third - number of balls in the line
        arr[2] = 0

        for (k in 0..TABLESIZE - 1) if (table.isEquals(k, y, c)) {
            arr[2]++
            if (arr[0] == -1) arr[0] = k
        } else if (arr[2] >= MIN_BALLS_TO_DESTROY) break
        else {
            arr[2] = 0; arr[0] = -1
        }

        if (arr[2] < MIN_BALLS_TO_DESTROY) {
            arr[0] = -1; arr[2] = 0
        }
        if (arr[2] != 0) arr[1] = y
        return arr
    }

    fun checkDiagOne(x: Int, y: Int, c: Int): Array<Int> {
        val arr = Array<Int>(3) { -1 }
        arr[2] = 0
        val dif = (y - x)

        for (k in 0..TABLESIZE - 1) {
            if ((k + dif < 0) || (k + dif > TABLESIZE - 1)) continue
            if (table.isEquals(k, k + dif, c)) {
                arr[2]++; if (arr[0] == -1) arr[0] = k
            } else if (arr[2] >= MIN_BALLS_TO_DESTROY) break
            else {
                arr[2] = 0; arr[0] = -1
            }
        }
        if (arr[2] < MIN_BALLS_TO_DESTROY) {
            arr[0] = -1; arr[2] = 0
        }
        if (arr[2] != 0) arr[1] = arr[0] + dif
        return arr
    }

    fun checkDiagTwo(x: Int, y: Int, c: Int): Array<Int> {
        val arr = Array<Int>(3) { -1 }
        arr[2] = 0
        val dif = (x + y)

        for (k in 0..TABLESIZE - 1) {
            if ((dif - k > TABLESIZE - 1) || (dif - k < 0)) continue
            if (table.isEquals(k, dif - k, c)) {
                arr[2]++; if (arr[0] == -1) arr[0] = k
            } else if (arr[2] >= MIN_BALLS_TO_DESTROY) break
            else {
                arr[2] = 0; arr[0] = -1
            }
        }
        if (arr[2] < MIN_BALLS_TO_DESTROY) {
            arr[0] = -1; arr[2] = 0
        }
        if (arr[2] != 0) arr[1] = dif - arr[0]
        return arr
    }


    fun destroyBalls(arr: Array<Array<Int>>) {
        if (arr[0][2] != 0) {
            for (k in arr[0][1]..arr[0][1] + arr[0][2] - 1) table.setValue(arr[0][0], k, 0)
        }
        if (arr[1][2] != 0) {
            for (k in arr[1][0]..arr[1][0] + arr[1][2] - 1) table.setValue(k, arr[1][1], 0)
        }
        if (arr[2][2] != 0) {
            for (k in 0..arr[2][2] - 1) table.setValue(k + arr[2][0], k + arr[2][1], 0)
        }
        if (arr[3][2] != 0) {
            for (k in 0..arr[3][2] - 1) table.setValue(arr[3][0] + k, arr[3][1] - k, 0)
        }
    }
}