function removeComments(email) {
    // remove comments
    var result = email.toLowerCase()
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
            if (new RegExp('^.+@.+$').test(item.value)) {
                item.valid = true
                item.transformed = removeComments(item.value)
            }
        }
    }
    return context
}