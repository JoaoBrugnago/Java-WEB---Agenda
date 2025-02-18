function confirmar(idcon) {
    let resposta = confirm("Confirma a exclusão deste contato?")
    if (resposta === true) {
        //window.alert("id: " + idcon)
        window.location.href = "delete?idcon=" + idcon
    }
}