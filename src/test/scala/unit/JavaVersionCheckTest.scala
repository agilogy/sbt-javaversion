package unit

import com.agilogy.sbt.JavaVersionCheck
import org.scalatest.FunSpec

class JavaVersionCheckTest extends FunSpec{

  describe("JavaVersionCheck") {

    it("should compare java versions correctly") {
      assert(JavaVersionCheck.isOk("1.8", "1.8") === true)
      assert(JavaVersionCheck.isOk("1.8", "1.7") === false)
      assert(JavaVersionCheck.isOk("1.7", "1.7.1") === true)
      assert(JavaVersionCheck.isOk("1.7.0", "1.7.1") === true)
      assert(JavaVersionCheck.isOk("1.8.0", "1.7") === false)
      assert(JavaVersionCheck.isOk("1.7.5", "1.8") === true)

    }

    it("should take the update number into account"){
      assert(JavaVersionCheck.isOk("1.8", "1.8.0_25") === true)
      assert(JavaVersionCheck.isOk("1.8.0", "1.8.0_25") === true)
      assert(JavaVersionCheck.isOk("1.8.0_25", "1.8.0_25") === true)
      assert(JavaVersionCheck.isOk("1.8.0_25", "1.8.0_27") === true)
      assert(JavaVersionCheck.isOk("1.8.0_25", "1.8.0_23") === false)
    }

  }
}
