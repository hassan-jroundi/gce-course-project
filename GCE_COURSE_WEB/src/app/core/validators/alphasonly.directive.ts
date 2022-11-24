import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({ selector: '[AlphasOnly]' })
export class AlphasOnlyDirective { 

    
    
    previousValue: string = '';

    // --------------------------------------
    //  Regular expressions
    onlyAlphasRegexp: string = '^[a-zA-Z\x7f-\xff -]*$';
    

    /**
     * Class constructor
     * @param hostElement
     */
    constructor(private hostElement: ElementRef) { }

    /**
     * Event handler for host's change event
     * @param e
     */
    @HostListener('change', ['$event']) onChange(e) {
        this.validateValue(this.hostElement.nativeElement.value);
    }

/**
 * Event handler for host's paste event
 * @param e
 */
@HostListener('paste', ['$event']) onPaste(e) {

    // get and validate data from clipboard
    let value = e.clipboardData.getData('text/plain');
    this.validateValue(value);
    e.preventDefault();
}

/**
 * Event handler for host's keydown event
 * @param event
 */
@HostListener('keydown', ['$event']) onKeyDown(e: KeyboardEvent) {

    let cursorPosition: number = e.target['selectionStart'];
    let originalValue: string = e.target['value'];
    let key: string = this.getName(e);
    let controlOrCommand = (e.ctrlKey === true || e.metaKey === true);
    
    

    // allowed keys apart from numeric characters
    let allowedKeys = [
        'Backspace', 'ArrowLeft', 'ArrowRight', 'Escape', 'Tab'
    ];

    // allow some non-numeric characters
    if (allowedKeys.indexOf(key) != -1 ||
        // Allow: Ctrl+A and Command+A
        (key == 'a' && controlOrCommand) ||
        // Allow: Ctrl+C and Command+C
        (key == 'c' && controlOrCommand) ||
        // Allow: Ctrl+V and Command+V
        (key == 'v' && controlOrCommand) ||
        // Allow: Ctrl+X and Command+X
        (key == 'x' && controlOrCommand)) {
        // let it happen, don't do anything
        return;
    }

    // save value before keydown event
    this.previousValue = originalValue;

    // allow number characters only
    let isAlpha = (new RegExp(this.onlyAlphasRegexp)).test(key);
    if (isAlpha) return; else e.preventDefault();
}

/**
 * Test whether value is a valid number or not
 * @param value
 */
validateValue(value: string): void {

    // choose the appropiate regular expression
    let regex: string = this.onlyAlphasRegexp;
    

    // when a numbers begins with a decimal separator,
    // fix it adding a zero in the beginning
    let firstCharacter = value.charAt(0);    

    // when a numbers ends with a decimal separator,
    // fix it adding a zero in the end
    let lastCharacter = value.charAt(value.length-1);    

    // test number with regular expression, when
    // number is invalid, replace it with a zero
    let valid: boolean = (new RegExp(regex)).test(value);
    this.hostElement.nativeElement['value'] = valid ? value : 0;
}

/**
 * Get key's name
 * @param e
 */
getName(e): string {

    if (e.key) {

        return e.key;

    } else {

        // for old browsers
        if (e.keyCode && String.fromCharCode) {

            switch (e.keyCode) {
                case   8: return 'Backspace';
                case   9: return 'Tab';
                case  27: return 'Escape';
                case  37: return 'ArrowLeft';
                case  39: return 'ArrowRight';
                case 188: return ',';
                case 190: return '.';
                case 109: return '-'; // minus in numbpad
                case 173: return '-'; // minus in alphabet keyboard in firefox
                case 189: return '-'; // minus in alphabet keyboard in chrome
                default: return String.fromCharCode(e.keyCode);
            }
        }
    }
}
}