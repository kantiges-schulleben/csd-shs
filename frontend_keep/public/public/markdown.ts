function parseMarkdown(content: string = ''): string {
    let contentToOutput: string = '';

    let markdown: string[] = ['\\', '*', '~', '-', '_', '=', '#'];
    let correspondingHTML: string[] = [
        '</br>',
        '',
        '<b>',
        '</b>',
        '<i>',
        '</i>',
        '<s>',
        '</s>',
        '<u>',
        '</u>',
        '<hr>',
        '',
        '<h5><hr>',
        '</h5><hr>',
    ];
    let status: boolean[] = markdown.map(() => false);

    for (let i = 0; i < content.length; i++) {
        const char: string = content[i];
        const indexOfChar: number = markdown.indexOf(char);

        if (indexOfChar > -1) {
            if (i < content.length - 2 && content[i + 1] !== ' ') {
                contentToOutput +=
                    correspondingHTML[
                        indexOfChar * 2 + (status[indexOfChar] ? 1 : 0)
                    ];
                if (correspondingHTML[indexOfChar * 2 + 1] !== '') {
                    status[indexOfChar] = !status[indexOfChar];
                }
            } else {
                contentToOutput += char;
            }
        } else {
            contentToOutput += char;
        }
    }

    for (let i = 0; i < status.length; i++) {
        if (status[i]) {
            contentToOutput += correspondingHTML[i * 2 + 1];
            status[i] = false;
        }
    }

    return contentToOutput;
}

/*
  export function parseMarkdown(content: string = ''): string {
      let contentToOutput: string = '';
  
      let boldStart: boolean = true;
      let italicStart: boolean = true;
      let durchStart: boolean = true;
      let underLineStart: boolean = true;
      let headlineStart: boolean = true;
      let ignoreNext: boolean = false;
  
      for (let i = 0; i < content.length; i++) {
          const char: string = content[i];
          switch (char) {
              // Zeilenumbruch
              case '\\':
                  if (content[i + 1] === ' ') {
                      contentToOutput += char;
                  } else {
                      contentToOutput += '</br>';
                  }
                  break;
              // fett
              case '*':
                  if (content[i + 1] === ' ' && boldStart === true) {
                      contentToOutput += char;
                  } else {
                      contentToOutput +=
                          '<' + (boldStart === true ? '' : '/') + 'b>';
                      boldStart = !boldStart;
                  }
                  break;
              // kursiv
              case '~':
                  if (content[i + 1] === ' ' && italicStart === true) {
                      contentToOutput += char;
                  } else {
                      contentToOutput +=
                          '<' + (italicStart === true ? '' : '/') + 'i>';
                      italicStart = !italicStart;
                  }
                  break;
              // durchgestrichen
              case '-':
                  if (content[i + 1] === ' ' && durchStart === true) {
                      contentToOutput += char;
                  } else {
                      contentToOutput +=
                          '<' + (durchStart === true ? '' : '/') + 's>';
                      durchStart = !durchStart;
                  }
                  break;
              // unterstrichen
              case '_':
                  if (content[i + 1] === ' ' && underLineStart === true) {
                      contentToOutput += char;
                  } else {
                      contentToOutput +=
                          '<' + (underLineStart === true ? '' : '/') + 'u>';
                      underLineStart = !underLineStart;
                  }
                  break;
              // horizontale Linie
              case '=':
                  if (ignoreNext === false) {
                      if (content[i + 1] !== '=') {
                          contentToOutput += char;
                      } else {
                          contentToOutput += '<hr>';
                          ignoreNext = true;
                      }
                  } else {
                      ignoreNext = false;
                  }
                  break;
              // Übershrift
              case '#':
                  if (content[i + 1] === ' ' && headlineStart === true) {
                      contentToOutput += char;
                  } else {
                      contentToOutput +=
                          '<' + (headlineStart === true ? '' : '/') + 'h5><hr>';
                      headlineStart = !headlineStart;
                  }
                  break;
              // Rest
              default:
                  contentToOutput += char;
                  break;
          }
      }
  
      if (boldStart === false) {
          contentToOutput += '</b>';
      }
      if (italicStart === false) {
          contentToOutput += '</i>';
      }
      if (durchStart === false) {
          contentToOutput += '</s>';
      }
      if (underLineStart === false) {
          contentToOutput += '</u>';
      }
      if (headlineStart === false) {
          contentToOutput += '</h5><hr>';
      }
  
      return contentToOutput;
  }*/
