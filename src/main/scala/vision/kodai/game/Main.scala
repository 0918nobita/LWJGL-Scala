package vision.kodai.game

import glfw.Builder

object Main {
  def main(args: Array[String]): Unit = {
    val window =
      Builder().withWidth(600).withHeight(400).withTitle("Game").build
    println(window) // => Window (600, 400, `Game`)

    variance()

    System.setProperty("org.lwjgl.system.allocator", "system")
    System.setProperty("org.lwjgl.util.Debug", "true")
    new Game().run()
  }

  private def variance(): Unit = {
    class Creature
    class Animal extends Creature
    class Cat    extends Animal

    class Container[-T, +R] {
      // 上: スーパークラス側
      // 下: サブクラス側
      // 上限境界 <: で反変の型引数を戻り値側で使えるようにする
      // T1 は T またはそのサブタイプ
      def foo[T1 <: T](arg: T1): T1 = arg

      // 下限境界 >: で共変の型引数を引数側で使えるようにする
      // R1 は R のスーパータイプ
      def bar[R1 >: R](arg: R1): Unit = {}
    }

    val c: Container[Animal, Creature] = new Container[Creature, Animal]
    // 仮に Container に「引数に R 型の値を取る f1 メソッド」が備わっていたとして、それを呼び出す際の引数について考える
    // c.f1(arg) の arg は、ユーザー側からすれば Creature (実際には Animal)
    // Creature を継承して Animal を定義しているので、定義側では Animal で新たに定義しているメンバに
    // アクセスできるはずなのに実際には存在していないという矛盾が発生する
    // このような理由から、共変の型引数はメソッドの引数側では使えない

    // 仮に Container に「戻り地が R 型の f2 メソッド」が備わっていたとして、それを呼び出した際の戻り値について考える
    // c.f2() の戻り型は、ユーザー側からすれば Creature なので、実際には Animal が返るが矛盾は生じない
    // Creature のメンバは、それを継承している Animal にもすべて存在している
    // このような理由から、共変の型引数はメソッドの戻り値側では使える

    // 仮に Container に「戻り値が T 型の f3 メソッド」が備わっていたとして、それを呼び出した際の戻り値について考える
    // c.f3() の戻り型は、ユーザー側からすれば Animal (実際には Creature)
    // Creature を継承して Animal を定義しているので、ユーザー側では Animal で新たに定義しているメンバに
    // アクセスできるはずなのに実際には存在していないという矛盾が発生する
    // このような理由から、反変の型引数をメソッドの戻り値側では使えない

    // 仮に Container に「引数に T 型の値を取る f4 メソッド」が備わっていたとして、それを呼び出す際の引数について考える
    // c.f4(arg) の arg は、ユーザー側からすれば Animal
    // 引数は定義側では Animal としての扱いを受けるので、Creature を渡した場合でも十分対応できる
    // このような理由から、反変の型引数はメソッドの引数側では使える
  }
}
