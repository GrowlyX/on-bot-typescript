/**
 * Sets the position of the caret in a text-editable element.
 * @author TechnoDrive
 * @param id id of the target **`text-editable`** element
 * @param position index of the
 */
export const caretToPosition = <T extends HTMLInputElement>(id: string, position: number) => {
    const element = document.getElementById(id) as T

    // "=== 0" required for firefox
    if (element.selectionStart || element.selectionStart === 0) {
        element.focus()
        element.setSelectionRange(position, position)
    }
}
