package com.example.gamelines

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.HighScore
import kotlinx.android.synthetic.main.activity_main.Score

//numbers-colors: 1 - red, 2 - green, 3 - blue, 4 - orange, 5 - purple, 6 - brown, 7 - sky
var ScoreSTR = "0"//current score
var HighScoreSTR = "0"//high score
val TABLESIZE = 9//size of game field
var Table = Array(TABLESIZE, {Array(TABLESIZE, {0})})//game field
var NEW_BALLS = 3 //how many new balls will appear
var threeBalls = Array(NEW_BALLS, {0})//color of random balls under table
var isFutureBalls = true //does buttonShow show random balls
val HOW_MANY_BALLS = 5 //how many balls will be destroyed by game
val NUMBER_OF_COLORS = 7 //how many colors of balls available

//show changes
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Block.visibility = VISIBLE
        Block.bringToFront()
    }
    fun rotate(view: View){
        if (requestedOrientation == SCREEN_ORIENTATION_PORTRAIT) setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE)
        else setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT)
    }

    //Block of start, process and end game
    fun startGame(view: View){
        RandomBallsCreation().randColor()
        Points().ClearPoints()
        GameTable().ClearTable()
        view.visibility = GONE
        Block.visibility = GONE
        Score.setText(ScoreSTR)
        Prize.visibility = GONE
        buttonShow.visibility = VISIBLE
        button3.visibility = VISIBLE
        for (x in 0..TABLESIZE-1)
            for (y in 0..TABLESIZE-1){
                val tabRow = tableLayout.getChildAt(x) as TableRow
                val cell = tabRow.getChildAt(y) as ImageView
                cell.isClickable = true
            }
        RandomBallsCreation().randomCellBall()
        drawRand()
        showTable()
    }
    fun game(){
        RandomBallsCreation().randomCellBall()
        showTable()
        showPoints()
        var emptyCells = 0
        for (x in 0..TABLESIZE-1) for (y in 0..TABLESIZE-1) if (Table[x][y] == 0) emptyCells++
        if (emptyCells > 0) {drawRand()}
        else {
            gameOver()
        }
    }
    fun gameOver(){
        for (x in 0..TABLESIZE-1)
            for (y in 0..TABLESIZE-1){
                val tabRow = tableLayout.getChildAt(x) as TableRow
                val cell = tabRow.getChildAt(y) as ImageView
                cell.isClickable = false
            }
        Prize.visibility = VISIBLE
        Block2.bringToFront()
        Yours.bringToFront()
        TotalScore.bringToFront()
        Nickname.bringToFront()
        Best.bringToFront()
        TotalBestScore.bringToFront()
        ButtonPublic.bringToFront()
        ButtonRestart.bringToFront()
        buttonShow.visibility = INVISIBLE
        button3.visibility = GONE
        TotalScore.setText(ScoreSTR)
        TotalBestScore.setText(HighScoreSTR)
    }
    fun publish(view: View){
        Toast.makeText(applicationContext, "в разработке", Toast.LENGTH_LONG).show()
    }

    //Function for earn tags in IntArray
    fun findByIdString(text: String):IntArray{
        val arr = IntArray(2)
        arr[0] = text[text.length-2].toInt()-'0'.toInt()
        arr[1] = text[text.length-1].toInt()-'0'.toInt()
        return arr
    }

    //Block of showing things on screen
    fun showPoints(){
        Score.setText(ScoreSTR)
        HighScore.setText(HighScoreSTR)
    }
    fun showTable(){
        for (x in 0..TABLESIZE-1)
            for (y in 0..TABLESIZE-1){
                val tabRow = tableLayout.getChildAt(x) as TableRow
                val cell = tabRow.getChildAt(y) as ImageView
                val color = Table[x][y]
                when(color){
                    0 -> cell.setImageDrawable(getDrawable(R.drawable.empty))
                    1 -> cell.setImageDrawable(getDrawable(R.drawable.redbubble))
                    2 -> cell.setImageDrawable(getDrawable(R.drawable.greenbubble))
                    3 -> cell.setImageDrawable(getDrawable(R.drawable.bluebubble))
                    4 -> cell.setImageDrawable(getDrawable(R.drawable.orangebubble))
                    5 -> cell.setImageDrawable(getDrawable(R.drawable.purplebubble))
                    6 -> cell.setImageDrawable(getDrawable(R.drawable.brownbubble))
                    7 -> cell.setImageDrawable(getDrawable(R.drawable.skybubble))
                }
            }
        showPoints()
    }

    //Block of bubbles under table
    fun isShown():Boolean{
        if (bubble1.visibility == VISIBLE) return true else return false
    }
    fun showB(){
        textNext.visibility = INVISIBLE
        bubble1.visibility = INVISIBLE
        bubble2.visibility = INVISIBLE
        bubble3.visibility = INVISIBLE
        val s = """Показать будущие
            |шарики""".trimMargin()
        buttonShow.setText(s)
        isFutureBalls = false
    }
    fun unshowB(){
        textNext.visibility = VISIBLE
        bubble1.visibility = VISIBLE
        bubble2.visibility = VISIBLE
        bubble3.visibility = VISIBLE
        val s = """Скрыть будущие
            |шарики""".trimMargin()
        buttonShow.setText(s)
        isFutureBalls = true
    }
    fun showBubbles(view: View) {
        if (isShown()) showB() else unshowB()
    }
    fun drawRand(){
        when(threeBalls[0]){
            1 -> bubble1.setImageDrawable(getDrawable(R.drawable.redbubble))
            2 -> bubble1.setImageDrawable(getDrawable(R.drawable.greenbubble))
            3 -> bubble1.setImageDrawable(getDrawable(R.drawable.bluebubble))
            4 -> bubble1.setImageDrawable(getDrawable(R.drawable.orangebubble))
            5 -> bubble1.setImageDrawable(getDrawable(R.drawable.purplebubble))
            6 -> bubble1.setImageDrawable(getDrawable(R.drawable.brownbubble))
            7 -> bubble1.setImageDrawable(getDrawable(R.drawable.skybubble))
        }
        when(threeBalls[1]){
            1 -> bubble2.setImageDrawable(getDrawable(R.drawable.redbubble))
            2 -> bubble2.setImageDrawable(getDrawable(R.drawable.greenbubble))
            3 -> bubble2.setImageDrawable(getDrawable(R.drawable.bluebubble))
            4 -> bubble2.setImageDrawable(getDrawable(R.drawable.orangebubble))
            5 -> bubble2.setImageDrawable(getDrawable(R.drawable.purplebubble))
            6 -> bubble2.setImageDrawable(getDrawable(R.drawable.brownbubble))
            7 -> bubble2.setImageDrawable(getDrawable(R.drawable.skybubble))
        }
        when(threeBalls[2]){
            1 -> bubble3.setImageDrawable(getDrawable(R.drawable.redbubble))
            2 -> bubble3.setImageDrawable(getDrawable(R.drawable.greenbubble))
            3 -> bubble3.setImageDrawable(getDrawable(R.drawable.bluebubble))
            4 -> bubble3.setImageDrawable(getDrawable(R.drawable.orangebubble))
            5 -> bubble3.setImageDrawable(getDrawable(R.drawable.purplebubble))
            6 -> bubble3.setImageDrawable(getDrawable(R.drawable.brownbubble))
            7 -> bubble3.setImageDrawable(getDrawable(R.drawable.skybubble))
        }
    }

    //Block of selection current ball and for his moving
    var isYellowBlock: Boolean = false //existence of selected ball
    var yellowCell = IntArray(2) //coordinates of selected ball
    var yellowType = 0 //type of selected ball, empty by default
    fun yellowBack(view: View) {
        val image = view as ImageView
        if (isYellowBlock == true) {
            if (image.drawable.constantState == getDrawable(R.drawable.empty)!!.constantState){
                if (PathCalculation().pathAccess(yellowCell, findByIdString(image.getTag().toString()))){
                    GameTable().paintEmpty(yellowCell, yellowType, findByIdString(image.getTag().toString()))
                    isYellowBlock = false
                    yellowType = 0
                    val sum = BallsDestroying().checkLines(findByIdString(image.getTag().toString()))
                    showTable()
                    Points().AddPoints(sum)
                    showPoints()
                    if (sum==0) game()
                }
                //for case "no way for ball"
                else {
                    var timer= object: CountDownTimer(300, 1000){
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
                    val tabRow = tableLayout.getChildAt(yellowCell[0]) as TableRow
                    val cell = tabRow.getChildAt(yellowCell[1]) as ImageView //желтая ячейка
                    if (cell.getTag() == image.getTag()) dropColor(image)
                    else paintBall(yellowCell, yellowType, image)
                }
            }
        }
        //for case "1-st choosing ball"
        else {
            if (image.drawable.constantState != getDrawable(R.drawable.empty)!!.constantState){
                newColor(image)
                yellowCell = findByIdString(view.getTag().toString())
                isYellowBlock = true
            }
        }
    }
    fun paintBall(coords: IntArray, color: Int, selected: ImageView){//changing selected ball, "selected" - new cell
        val tabRow = tableLayout.getChildAt(coords[0]) as TableRow
        val cell = tabRow.getChildAt(coords[1]) as ImageView //selected ball
        when (color){//delete selection of a ball
            1 -> cell.setImageDrawable(getDrawable(R.drawable.redbubble))
            2 -> cell.setImageDrawable(getDrawable(R.drawable.greenbubble))
            3 -> cell.setImageDrawable(getDrawable(R.drawable.bluebubble))
            4 -> cell.setImageDrawable(getDrawable(R.drawable.orangebubble))
            5 -> cell.setImageDrawable(getDrawable(R.drawable.purplebubble))
            6 -> cell.setImageDrawable(getDrawable(R.drawable.brownbubble))
            7 -> cell.setImageDrawable(getDrawable(R.drawable.skybubble))
        }
        changeColor(selected)
        yellowCell = findByIdString(selected.getTag().toString())//changing coordinates of selected ball
    }
    fun newColor(view: ImageView){
        changeColor(view)
        isYellowBlock = true
    }
    fun dropColor(view: ImageView){
        when (view.drawable.constantState){
            getDrawable(R.drawable.redbubble_y)!!.constantState -> view.setImageDrawable(getDrawable(R.drawable.redbubble))
            getDrawable(R.drawable.greenbubble_y)!!.constantState -> view.setImageDrawable(getDrawable(R.drawable.greenbubble))
            getDrawable(R.drawable.bluebubble_y)!!.constantState -> view.setImageDrawable(getDrawable(R.drawable.bluebubble))
            getDrawable(R.drawable.orangebubble_y)!!.constantState -> view.setImageDrawable(getDrawable(R.drawable.orangebubble))
            getDrawable(R.drawable.purplebubble_y)!!.constantState -> view.setImageDrawable(getDrawable(R.drawable.purplebubble))
            getDrawable(R.drawable.brownbubble_y)!!.constantState -> view.setImageDrawable(getDrawable(R.drawable.brownbubble))
            getDrawable(R.drawable.skybubble_y)!!.constantState -> view.setImageDrawable(getDrawable(R.drawable.skybubble))
        }
        yellowType = 0
        isYellowBlock = false
    }

    //ball's selection
    fun changeColor(view: ImageView){
        when (view.drawable.constantState) {
            getDrawable(R.drawable.redbubble)!!.constantState -> {view.setImageDrawable(getDrawable(R.drawable.redbubble_y)); yellowType = 1}
            getDrawable(R.drawable.greenbubble)!!.constantState -> {view.setImageDrawable(getDrawable(R.drawable.greenbubble_y)); yellowType = 2}
            getDrawable(R.drawable.bluebubble)!!.constantState -> {view.setImageDrawable(getDrawable(R.drawable.bluebubble_y)); yellowType = 3}
            getDrawable(R.drawable.orangebubble)!!.constantState -> {view.setImageDrawable(getDrawable(R.drawable.orangebubble_y)); yellowType = 4}
            getDrawable(R.drawable.purplebubble)!!.constantState -> {view.setImageDrawable(getDrawable(R.drawable.purplebubble_y)); yellowType = 5}
            getDrawable(R.drawable.brownbubble)!!.constantState -> {view.setImageDrawable(getDrawable(R.drawable.brownbubble_y)); yellowType = 6}
            getDrawable(R.drawable.skybubble)!!.constantState -> {view.setImageDrawable(getDrawable(R.drawable.skybubble_y)); yellowType = 7}
        }
    }
}

//change table as global variable
class GameTable{
    fun ClearTable(){
        for (x in 0..TABLESIZE-1)
            for (y in 0..TABLESIZE-1)
                Table[x][y] = 0}
    fun paintEmpty(coords: IntArray, color: Int, selected: IntArray){//moving ball into empty cell
        Table[selected[0]][selected[1]] = color
        Table[coords[0]][coords[1]] = 0
    }
}

//Lee's algorithm
class PathCalculation{
    fun pathAccess(yellowCoords: IntArray, coords: IntArray):Boolean{ //checking that the ball can move from the starting position to the selected empty cell
        var table : Array<Array<Int>> = Array(TABLESIZE, {Array(TABLESIZE, {0})})
        for (x in 0..TABLESIZE-1)
            for (y in 0..TABLESIZE-1){
                if (Table[x][y] != 0) table[x][y] = -1
            }
        table[yellowCoords[0]][yellowCoords[1]] = 1
        var cnt = 1
        while (table[coords[0]][coords[1]] == 0) {
            path(table, cnt)
            cnt++
            if (cnt>=TABLESIZE*TABLESIZE) break
        }
        if (table[coords[0]][coords[1]] != 0) return true
        else return false
    }
    fun path(table: Array<Array<Int>>, num: Int){
        var count = 0
        for(x in 0..TABLESIZE-1) for(y in 0..TABLESIZE-1) if (table[x][y] == num) count+=2
        if (count!=1){
            val arr = IntArray(count)
            var n = 0
            for(x in 0..TABLESIZE-1)
                for(y in 0..TABLESIZE-1) if (table[x][y] == num) {
                    arr[n] = x
                    n++
                    arr[n] = y
                    n++}
            for(i in 0..n-1 step 2) {
                val x = arr[i]; val y = arr[i+1]
                if (x-1>=0) if (table[x-1][y] == 0) table[x-1][y] = num+1
                if (y-1>=0) if (table[x][y-1] == 0) table[x][y-1] = num+1
                if (x+1<=TABLESIZE-1) if (table[x+1][y] == 0) table[x+1][y] = num+1
                if (y+1<=TABLESIZE-1) if (table[x][y+1] == 0) table[x][y+1] = num+1
            }
        }
    }
}

//creating new balls and colors
class RandomBallsCreation{
    fun randColor() {threeBalls = Array(3,{(1..NUMBER_OF_COLORS).random()})}
    fun randomCellBall(){
        var k = 0
        while(k < NEW_BALLS){ //NEW_BALLS - number of a new balls, 3 by default
            val x = (0..TABLESIZE-1).random()
            val y = (0..TABLESIZE-1).random()
            if (Table[x][y] == 0) {
                Table[x][y] = threeBalls[k]
                val sum = BallsDestroying().checkLines(intArrayOf(x,y))
                if (sum > 0) {
                    Points().AddPoints(sum)
                }
                var free = 0
                for (i in 0..TABLESIZE-1)
                    for (j in 0..TABLESIZE-1)
                        if (Table[i][j] == 0) free+=1
                if (free !=0 ) k++ else break
            }
        }
        randColor()
    }
}

//work with global variables of points
class Points{
    fun TableOfPoints(balls: Int): Int{
        when (balls){
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
    fun AddPoints(point: Int){
        ScoreSTR = (ScoreSTR.toInt() + TableOfPoints(point)).toString()
        if (HighScoreSTR.toInt() < ScoreSTR.toInt()) HighScoreSTR = ScoreSTR
    }
    fun ClearPoints(){
        ScoreSTR = "0"
    }
}

//checking lines and destroying them
class BallsDestroying{
    fun checkLines(X: IntArray): Int{
        var sum = 0
        val x = X[0]
        val y = X[1]
        val color = Table[x][y]
        var destroyArr : Array<Array<Int>> = Array(4, { Array(3, {-1}) })
        destroyArr[0] = checkHoriz(x, y, color)
        destroyArr[1] = checkVert(x, y, color)
        destroyArr[2] = checkDiagOne(x, y, color)
        destroyArr[3] = checkDiagTwo(x, y, color)
        for(k in 0..3)
            if (sum==0) sum+=destroyArr[k][2]
            else if (destroyArr[k][2]!=0) sum+=destroyArr[k][2]-1
        if (sum !=0) destroyBalls(destroyArr)
        return sum
    }

    //Block of checking in 4 directions
    fun checkHoriz(x: Int, y: Int, c: Int): Array<Int>{//at the input, the coordinates of the cell from which the counting starts and its color; at the output, the coordinates of the beginning of the line and its length
        var arr = Array(3){-1} //first - row number, second - column number, third - number of balls in the line
        arr[2] = 0
        for (k in 0..TABLESIZE-1) if (Table[x][k] == c) {
            arr[2]++
            if (arr[1] == -1) arr[1] = k
        }
        else if (arr[2]>= HOW_MANY_BALLS) break
        else {arr[2] = 0; arr[1] = -1}

        if (arr[2] <HOW_MANY_BALLS) {arr[1] =-1; arr[2] = 0}
        if (arr[2] != 0) arr[0] = x
        return arr
    }
    fun checkVert(x: Int, y: Int, c: Int): Array<Int>{
        var arr = Array(3){-1} //first - row number, second - column number, third - number of balls in the line
        arr[2] = 0

        for (k in 0..TABLESIZE-1) if (Table[k][y] == c) {
            arr[2]++
            if (arr[0] == -1) arr[0] = k
        }
        else if (arr[2]>= HOW_MANY_BALLS) break
        else {arr[2] = 0; arr[0] = -1}

        if (arr[2] <HOW_MANY_BALLS) {arr[0] =-1; arr[2] = 0}
        if (arr[2] != 0) arr[1] = y
        return arr
    }
    fun checkDiagOne(x: Int, y: Int, c: Int): Array<Int>{
        var arr = Array<Int>(3){-1}
        arr[2] = 0
        val dif = (y-x)

        for (k in 0..TABLESIZE-1){
            if ((k+dif <0)||(k+dif >TABLESIZE-1)) continue
            if (Table[k][k+dif] == c) {arr[2]++; if (arr[0]==-1) arr[0] = k}
            else if (arr[2]>=HOW_MANY_BALLS) break
            else {arr[2]=0; arr[0] =-1}
        }
        if (arr[2] <HOW_MANY_BALLS) {arr[0] = -1; arr[2] = 0}
        if (arr[2] != 0) arr[1] = arr[0]+dif
        return arr
    }
    fun checkDiagTwo(x: Int, y: Int, c: Int): Array<Int>{
        var arr = Array<Int>(3){-1}
        arr[2] = 0
        val dif = (x+y)

        for (k in 0..TABLESIZE-1){
            if ((dif-k >TABLESIZE-1)||(dif-k <0)) continue
            if (Table[k][dif-k] == c) {arr[2]++; if (arr[0]==-1) arr[0] = k}
            else if (arr[2]>=HOW_MANY_BALLS) break
            else {arr[2]=0; arr[0] =-1
            }
        }
        if (arr[2] <HOW_MANY_BALLS) {arr[0] = -1; arr[2] = 0}
        if (arr[2] != 0) arr[1] = dif-arr[0]
        return arr
    }


    fun destroyBalls(arr: Array<Array<Int>>){
        if (arr[0][2] != 0) {
            for (k in arr[0][1]..arr[0][1]+arr[0][2]-1) Table[arr[0][0]][k] = 0
        }
        if (arr[1][2] != 0) {
            for (k in arr[1][0]..arr[1][0]+arr[1][2]-1) Table[k][arr[1][1]] = 0
        }
        if (arr[2][2] != 0) {
            for (k in 0..arr[2][2]-1) Table[k+arr[2][0]][k+arr[2][1]] = 0
        }
        if (arr[3][2] != 0) {
            for (k in 0..arr[3][2]-1) Table[arr[3][0]+k][arr[3][1]-k] = 0
        }
    }
}







    //МУСОРКА

/*val x = threeBalls[0]; val y = threeBalls[1]; val z = threeBalls[2]
        Toast.makeText(applicationContext, "$x, $y, $z", Toast.LENGTH_SHORT).show()*/
/*fun destroyBalls(arr: Array<Array<Int>>){
        var n = 0
        if (arr[0][0] != -1) {
            val tabRow = tableLayout.getChildAt(arr[0][0]) as TableRow
            for (x in arr[0][1]..arr[0][1]+arr[0][2]-1) {
                val image = tabRow.getChildAt(x) as ImageView
                var timer= object: CountDownTimer(200, 1000){
                    override fun onTick(p0: Long) {
                        image.setImageDrawable(getDrawable(R.drawable.boom))
                    }
                    override fun onFinish() {
                        image.setImageDrawable(getDrawable(R.drawable.empty))
                    }
                }
                timer.start()
            }
            n+=arr[0][2]
        }
        if (arr[1][0] != -1) {
            for (x in arr[1][0]..arr[1][0]+arr[1][2]-1) {
                val tabRow = tableLayout.getChildAt(x) as TableRow
                val image = tabRow.getChildAt(arr[1][1]) as ImageView
                var timer= object: CountDownTimer(200, 1000){
                    override fun onTick(p0: Long) {
                        image.setImageDrawable(getDrawable(R.drawable.boom))
                    }
                    override fun onFinish() {
                        image.setImageDrawable(getDrawable(R.drawable.empty))
                    }
                }
                timer.start()
            }
            n+=arr[1][2]
        }
        if (arr[2][0] != -1) {
            for (x in 0..arr[2][2]-1){
                val tabRow = tableLayout.getChildAt(x+arr[2][0]) as TableRow
                val image = tabRow.getChildAt(x+arr[2][1]) as ImageView
                var timer= object: CountDownTimer(200, 1000){
                    override fun onTick(p0: Long) {
                        image.setImageDrawable(getDrawable(R.drawable.boom))
                    }
                    override fun onFinish() {
                        image.setImageDrawable(getDrawable(R.drawable.empty))
                    }
                }
                timer.start()
            }
            n+=arr[2][2]
        }
        if (arr[3][0] != -1) {
            for (x in 0..arr[3][2]-1){
                val tabRow = tableLayout.getChildAt(arr[3][0]+x) as TableRow
                val image = tabRow.getChildAt(arr[3][1]-x) as ImageView
                var timer= object: CountDownTimer(200, 1000){
                    override fun onTick(p0: Long) {
                        image.setImageDrawable(getDrawable(R.drawable.boom))
                    }
                    override fun onFinish() {
                        image.setImageDrawable(getDrawable(R.drawable.empty))
                    }
                }
                timer.start()
            }
            n+=arr[3][2]
        }
        emptyCells += n
    }
fun checkLines(view: ImageView): Int{
    var sum: Int = 0
    var destroyArr : Array<Array<Int>> = Array(4, { Array(3, {-1}) })
    destroyArr[0] = checkHoriz(view)
    destroyArr[1] = checkVert(view)
    destroyArr[2] = checkDiagOne(view)
    destroyArr[3] = checkDiagTwo(view)
    if (checkHoriz(view)[2]!=0) sum+= checkHoriz(view)[2]
    if (checkVert(view)[2]!=0) {
        if (sum!=0) sum--
        sum+= checkVert(view)[2]
    }
    if (checkDiagOne(view)[2]!=0) {
        if (sum!=0) sum--
        sum+= checkDiagOne(view)[2]
    }
    if (checkDiagTwo(view)[2]!=0) {
        if (sum!=0) sum--
        sum+= checkDiagTwo(view)[2]
    }
    if (sum !=0) {destroyBalls(destroyArr);
        if (buttonShow.text == "Скрыть будущие шарики") givePoints(Points(sum))
        else givePoints(Points(sum+1))
    }
    return sum
}*/
/*fun checkHoriz(selected: ImageView): Array<Int>{
        var arr = Array<Int>(3){-1} //первое число - номер строки, второе число - номер столбца, третье число - количество шаров в линии
        val coords = findByIdString(selected.getTag().toString())
        val row = tableLayout.getChildAt(coords[0]) as TableRow
        val color = selected.drawable.constantState
        arr[2] = 0
        for (x in 0..TABLESIZE-1){
            val cell = row.getChildAt(x) as ImageView
            if (cell.drawable.constantState == color) {arr[2]++; if (arr[1]==-1) arr[1] = x}
            else if (arr[2]>=HOW_MANY_BALLS) break
            else {arr[2]=0; arr[1] =-1}
        }
        if (arr[2] <HOW_MANY_BALLS) {arr[1] =-1; arr[2] = 0}
        if (arr[2] != 0) arr[0] = coords[0]

        return arr
    }
    fun checkVert(selected: ImageView): Array<Int>{
        var arr = Array<Int>(3){-1} //первое число - номер строки, второе число - номер столбца, третье число - количество шаров в линии
        val coords = findByIdString(selected.getTag().toString())
        val color = selected.drawable.constantState
        arr[2] = 0

        for (x in 0..TABLESIZE-1){
            val row = tableLayout.getChildAt(x) as TableRow
            val cell = row.getChildAt(coords[1]) as ImageView
            if (cell.drawable.constantState == color) {arr[2]++; if (arr[0]==-1) arr[0] = x}
            else if (arr[2]>=HOW_MANY_BALLS) break
            else {arr[2]=0; arr[0] =-1}
        }
        if (arr[2] <HOW_MANY_BALLS) {arr[0] = -1; arr[2] = 0}
        if (arr[2] != 0) arr[1] = coords[1]
        return arr
    }
    fun checkDiagOne(selected: ImageView): Array<Int>{
        var arr = Array<Int>(3){-1} //первое число - номер строки, второе число - номер столбца, третье число - количество шаров в линии
        val coords = findByIdString(selected.getTag().toString())
        val color = selected.drawable.constantState
        arr[2] = 0
        val dif = (coords[1]-coords[0])

        for (x in 0..TABLESIZE-1){
            if ((x+dif <0)||(x+dif >TABLESIZE-1)) continue
            val row = tableLayout.getChildAt(x) as TableRow
            val cell = row.getChildAt(x+dif) as ImageView
            if (cell.drawable.constantState == color) {arr[2]++; if (arr[0]==-1) arr[0] = x}
            else if (arr[2]>=HOW_MANY_BALLS) break
            else {arr[2]=0; arr[0] =-1}
        }
        if (arr[2] <HOW_MANY_BALLS) {arr[0] = -1; arr[2] = 0}
        if (arr[2] != 0) arr[1] = arr[0]+dif
        return arr
    }
    fun checkDiagTwo(selected: ImageView): Array<Int>{
        var arr = Array<Int>(3){-1} //первое число - номер строки, второе число - номер столбца, третье число - количество шаров в линии
        val coords = findByIdString(selected.getTag().toString())
        val color = selected.drawable.constantState
        arr[2] = 0
        val dif = (coords[0]+coords[1])

        for (x in 0..TABLESIZE-1){
            if ((dif-x >TABLESIZE-1)||(dif-x <0)) continue
            val row = tableLayout.getChildAt(x) as TableRow
            val cell = row.getChildAt(dif-x) as ImageView
            if (cell.drawable.constantState == color) {arr[2]++; if (arr[0]==-1) arr[0] = x}
            else if (arr[2]>=HOW_MANY_BALLS) break
            else {arr[2]=0; arr[0] =-1
            }
        }
        if (arr[2] <HOW_MANY_BALLS) {arr[0] = -1; arr[2] = 0}
        if (arr[2] != 0) arr[1] = dif-arr[0]
        return arr
    }*/
/*fun randomCellBall(){
        var k = 0
        while(k < NEW_BALLS){
            val rand1 = (0..TABLESIZE-1).random()
            val tabRow = tableLayout.getChildAt(rand1) as TableRow
            val rand2 = (0..TABLESIZE-1).random()
            val cell = tabRow.getChildAt(rand2) as ImageView
            if (cell.drawable.constantState == getDrawable(R.drawable.empty)!!.constantState) {
                when(k){
                    0 -> cell.setImageDrawable(bubble1.drawable)
                    1 -> cell.setImageDrawable(bubble2.drawable)
                    else -> cell.setImageDrawable(bubble3.drawable)
                }
                checkLines(cell)
                k++
            }
        }
        drawRand()
    }*/

//PaintEmpty:
/*val tabRow = tableLayout.getChildAt(coords[0]) as TableRow
        val cell = tabRow.getChildAt(coords[1]) as ImageView //желтая ячейка
        when (color){
            "blue" -> selected.setImageDrawable(getDrawable(R.drawable.bluebubble))
            "red" -> selected.setImageDrawable(getDrawable(R.drawable.redbubble))
            "green" -> selected.setImageDrawable(getDrawable(R.drawable.greenbubble))
            "orange" -> selected.setImageDrawable(getDrawable(R.drawable.orangebubble))
        }
        cell.setImageDrawable(getDrawable(R.drawable.empty))
        */
/*fun pathAccess(view: ImageView):Boolean{ //проверка, что шар может переместиться от указанной позиции до выбранной пустой ячейки
    var coords = findByIdString(view.getTag().toString())
    var table : Array<Array<Int>> = Array(SIZE, {Array(SIZE, {0})})
    var yellowCoords = IntArray(2)
    for (x in 0..SIZE-1) {
        val tabRow = tableLayout.getChildAt(x) as TableRow
        for (y in 0..SIZE-1){
            val cel = tabRow.getChildAt(y) as ImageView
            if (cel.drawable.constantState != getDrawable(R.drawable.empty)!!.constantState) table[x][y] = -1
        }
    }
    yellowCoords[0] = yellowCell[0]
    yellowCoords[1] = yellowCell[1]
    PathCalculation().path(yellowCoords, table,1, coords)

    if (table[coords[0]][coords[1]] != 0) return true
    else return false
}*/
/*class MyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val textView = TextView(activity)
            val str = "Ваш счёт:" + System.getProperty ("line.separator") + ScoreSTR + System.getProperty ("line.separator") + "Лучший счёт:"+ System.getProperty ("line.separator") + HighScoreSTR
            with(textView) {
                textView.setText (str)
                textView.textSize = 18.0F
                //textView.setTypeface(null, Typeface.BOLD)
                textView.gravity = Gravity.CENTER
            }
            builder.setTitle("Игра закончена")
                .setView(textView)
                .setCancelable(false)
                .setPositiveButton("Начать заново") {
                        dialog, id ->  dialog.cancel()
                }
                .setNegativeButton("Опубликовать"){
                        dialog, id -> dialog.cancel()
                }
            builder.setCancelable(false)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}*/
/*val myDialogFragment = MyDialogFragment()
        val manager = supportFragmentManager
        myDialogFragment.show(manager, "myDialog")*/
    //Toast.makeText(applicationContext, "", Toast.LENGTH_LONG).show()
    /*val MainActivity.displayRotation: Int get() = getWindowManager().getDefaultDisplay().getRotation()

    private val displaySize: Pair<Int, Int> get() {
        val display = getWindowManager().getDefaultDisplay()
        return if (Build.VERSION.SDK_INT >= 20) {
            val point = Point()
            display.getSize(point)
            point.x to point.y
        }
        else display.getWidth() to display.getHeight()
    }

    val MainActivity.displayOrientation: Int get() {
        val (width, height) = displaySize
        return if (width > height)
            Configuration.ORIENTATION_LANDSCAPE
        else
            Configuration.ORIENTATION_PORTRAIT
    }*/
    /*object : CountDownTimer(300, 1000) {

                            override fun onTick(millisUntilFinished: Long) {
                                image.setImageDrawable(getDrawable(R.drawable.wrong))
                            }

                            override fun onFinish() {
                                image.setImageDrawable(getDrawable(R.drawable.empty))
                            }
                        }.start()*/

/*fun drawRand(view: ImageView){val x1 = (1..4).random()
    when(x1){
        1 -> view.setImageDrawable(getDrawable(R.drawable.bluebubble))
        2 -> view.setImageDrawable(getDrawable(R.drawable.redbubble))
        3 -> view.setImageDrawable(getDrawable(R.drawable.greenbubble))
        4 -> view.setImageDrawable(getDrawable(R.drawable.orangebubble))
    }
    checkLines(view)
}*/
/*fun path(c: IntArray, table: Array<Array<Int>>, num: Int, coords: IntArray){
        var c1 = IntArray(2)
        var c2 = IntArray(2)
        var c3 = IntArray(2)
        var c4 = IntArray(2)
        var top = false; var bot = false; var left = false; var right = false
        if (c[0]-1 >=0) {//вверх на 1 клетку
            c1[0] = c[0]-1
            c1[1] = c[1]
            if (table[c1[0]][c1[1]] == 0) {table[c1[0]][c1[1]] = num; top = true}
        }
        if (c[1]-1 >=0) {//влево на 1 клетку
            c2[0] = c[0]
            c2[1] = c[1]-1
            if (table[c2[0]][c2[1]] == 0) {table[c2[0]][c2[1]] = num; left = true}
        }
        if (c[1]+1 <=SIZE-1) {//вправо на 1 клетку
            c3[0] = c[0]
            c3[1] = c[1]+1
            if (table[c3[0]][c3[1]] == 0) {table[c3[0]][c3[1]] = num; right = true}
        }
        if (c[0]+1 <=SIZE-1) {//вниз на 1 клетку
            c4[0] = c[0]+1
            c4[1] = c[1]
            if (table[c4[0]][c4[1]] == 0) {table[c4[0]][c4[1]] = num; bot = true}
        }
        if (table[coords[0]][coords[1]] == 0){
        if (top == true) path(c1, table, num+1, coords)
        if (left == true) path(c2, table, num+1, coords)
        if (right == true) path(c3, table, num+1, coords)
        if (bot == true) path(c4, table, num+1, coords)
        }
    }*/