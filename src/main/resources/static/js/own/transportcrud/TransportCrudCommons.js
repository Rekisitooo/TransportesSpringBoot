/**
 * Changes the display to none of any element (d-none)
 */
export function changeElementDisplayNone(element) {
    let elementClass = element.attr('class');

    if (elementClass.includes('d-none')) {
        elementClass = elementClass.replace('d-none', '');
    } else {
        elementClass += ' d-none';
    }

    return elementClass;
}

/**
 * Returns the element class changed.
 * @param {jQuery} element - The element to change
 * @param {string} replace - The class to replace
 * @param {string} toBeReplaced - The class to be replaced
 * @returns {string} The element class changed
 */
export function changeElementClass(element, replace, toBeReplaced) {
    let elementClass = element.attr('class');

    if (elementClass.includes(toBeReplaced)) {
        elementClass = elementClass.replace(toBeReplaced, replace);
    }

    return elementClass;
}