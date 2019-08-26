package br.com.packapps.rbcoletorandroid.model.response

class Error(var code : Int, var messages : MutableList<String>


) {
    override fun toString(): String {
        return "Error(code=$code, messages=${messages.toString()})"
    }
}

