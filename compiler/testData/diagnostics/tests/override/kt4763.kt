trait A {
    fun f(): String
}

open class B {
    open fun f(): CharSequence = "charSequence"
}

class <!ABSTRACT_MEMBER_NOT_IMPLEMENTED!>C<!> : B(), A




trait P {
    var f: Number
}

open class Q {
    val x: Int = 42
}

class <!ABSTRACT_MEMBER_NOT_IMPLEMENTED!>R<!> : P, Q()
