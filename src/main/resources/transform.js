function removeComments(email) {
    // remove comments
    var result = email.toUpperCase()
    var parts = result.split('@')
    var local = parts[0];
    result = local.replace('.', '')
        .replace(new RegExp('\\(.*\\)'), '')
        .replace(new RegExp('\{.*\}'),'')
        .replace(new RegExp('\\+.*$'),'')
    return result + '@' + parts[1]
}


function transform(context) {
    for each (var item in context.items) {
        if (item.key === 'email') {
            item.transformed = removeComments(item.value)
        }
    }
    return context
}