export function changeElementDisplayNone(element) {
    let elementClass = element.attr('class');

    if (elementClass.includes('d-none')) {
        elementClass = elementClass.replace('d-none', '');
    } else {
        elementClass += ' d-none';
    }

    return elementClass;
}

export function changeElementClass(element, needle, haystack) {
    let elementClass = element.attr('class');

    if (elementClass.includes(needle)) {
        elementClass = elementClass.replace(needle, haystack);
    }

    return elementClass;
}