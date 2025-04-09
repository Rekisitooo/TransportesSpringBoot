export function changeElementDisplayNone(element) {
    let elementClass = element.attr('class');

    if (elementClass.includes('d-none')) {
        elementClass = elementClass.replace('d-none', '');
    } else {
        elementClass += ' d-none';
    }

    return elementClass;
}

export function changeElementClass(element, replace, toBeReplaced) {
    let elementClass = element.attr('class');

    if (elementClass.includes(toBeReplaced)) {
        elementClass = elementClass.replace(toBeReplaced, replace);
    }

    return elementClass;
}