# CSE323 Final project -> TextEditor

A simple yet functional text editor built with Java Swing, designed to handle multiple file formats including programming languages and text documents.

## Table of Contents
- [Technical Stack](#technical-stack)
- [Key Features](#key-features)
- [How to Run Project](#how-to-run-project)
- [Project Setup](#project-setup)
- [Development Challenges](#development-challenges-star-format)


## Technical Stack

- **Language:** Java (JDK 8+)
- **GUI Framework:** Java Swing, Java AWT, AWT Events
- **File I/O:** BufferedReader, BufferedWriter, FileInputStream, FileReader, FileWriter
- **Data Handling:** Regular Expressions (Regex) for Syntax Highlighting
- **Layout Managers:** BorderLayout, FlowLayout
- **Components:** JFrame, JTextPane, JEditorPane, JMenuBar, JToolBar, JComboBox, JFileChooser, JScrollPane
- **Advanced API:** Java AWT Drag & Drop (dnd), ImageFilter (RGBImageFilter)
- **Theme Engine:** Custom Midnight Theme Implementation (Luma-based Masking, UIManager customization)

## Key Features

- **Universal File Support:** Open, Save, and Drag & Drop support for 12+ formats including Java, C, C++, Python, HTML, CSS, JS, SQL, PHP, XML, JSON, and Markdown.
- **Text Editing:** Cut, Copy, Paste, Select All, Clear, Undo/Redo
- **Formatting:** Bold text, Font selection (13 standard fonts), Size selection (15-100pt)
- **Search & Replace:** Sidebar-based real-time search with replacement capabilities
- **Statistics:** Real-time character, line, and word count
- **Drag & Drop:** Robust file dropping from OS file manager
- **Modern UI:** "Midnight" Dark Theme with consistent transparency and luma-masked icons
- **Universal Syntax Highlighting:** Real-time syntax coloring for all supported languages, recognizing keywords, strings, and multi-style comments (//, #, <!-- -->).
- **Advanced Markdown:** Specialized highlighting for Headers, Lists, Bold text, and Code Blocks.
- **Enhanced About Me:** Clickable links to GitHub profile and project repository

## How to Run Project

```bash
cd "/TextEditor/src/" && javac simplejavatexteditor/*.java && java simplejavatexteditor.SimpleJavaTextEditor
```

## Project Setup

### Prerequisites
- **Java Development Kit (JDK):** Version 8 or higher.
- **Git:** To clone the repository.

### Installation Steps
1. **Clone the Repository**
   ```bash
   git clone https://github.com/SafatUddin/CSE323_Project.git
   ```

2. **Navigate to the Project Directory**
   ```bash
   cd CSE323_Project
   ```

3. **Verify Directory Structure**
   Ensure you are in the root directory containing the `src` folder.


## Development Challenges (STAR Format)

### Challenge 1: Implementing a User-Friendly GUI with Proper Layout

**Situation:** At the beginning of the project, we needed to create an intuitive graphical user interface for the text editor. The challenge was to organize multiple components (menu bar, toolbar, text area, and various buttons) in a way that would be familiar to users and provide easy access to all features without cluttering the interface.

**Task:** Design and implement a complete GUI layout that includes a menu system, toolbar with icon buttons, font selection dropdowns, and a main text editing area, all while maintaining a professional appearance similar to standard text editors.

**Action:** 
1. Used Java Swing's BorderLayout to organize the main frame with toolbar at the top and text area in the center
2. Created a comprehensive menu bar with File, Edit, Search, and About menus
3. Designed a toolbar with icon buttons for frequently used operations (New, Open, Save, Clear, Search, About, Quit, Bold)
4. Integrated font type and size dropdown menus directly into the toolbar for easy access
5. Added text labels below icons using `setVerticalTextPosition()` and `setHorizontalTextPosition()` to clearly identify each button's function
6. Implemented proper spacing with separators between button groups
7. Used scroll pane for the text area to handle large documents

**Result:** Created a clean, intuitive interface that users can navigate easily. All major functions are accessible through both menu items and toolbar buttons, providing flexibility for different user preferences. The layout is responsive and adapts well to window resizing.

---

### Challenge 2: Managing File I/O Operations for Multiple File Types

**Situation:** The text editor needed to support opening and saving files in multiple formats (.txt, .java, .c, .cpp, .py, .doc, .docx). However, implementing file operations that could handle different file types while providing clear feedback to users about what formats are supported was complex.

**Task:** Implement robust file opening and saving functionality that allows users to work with multiple file types, with proper file filtering in the file chooser dialogs and error handling for unsupported operations.

**Action:**
1. Implemented `BufferedReader` and `BufferedWriter` for efficient file reading and writing operations
2. Created multiple `FileNameExtensionFilter` objects for each supported file type (Text, Java, C, C++, Python, Documents)
3. Added all filters to the `JFileChooser` using `addChoosableFileFilter()` for both open and save dialogs
4. Set "All Supported Files" as the default filter for opening to show all compatible files
5. Set ".txt" as the default filter for saving as it's the most common format
6. Implemented proper exception handling with error dialogs to inform users of file operation failures
7. Added debug logging to track file operations and help diagnose issues
8. Ensured proper resource cleanup with explicit `close()` calls and proper exception handling

**Result:** Users can seamlessly open and save files in various formats with clear visual feedback about supported file types. The file chooser presents organized categories, making it easy to filter files by extension. Error messages provide helpful feedback when operations fail, improving the overall user experience.

---

### Challenge 3: Designing a Flexible Font System with Standard Options

**Situation:** Users needed the ability to customize text appearance, but exposing all system fonts would be overwhelming and many fonts are not suitable for text editing. Additionally, we needed to establish appropriate defaults for font family and size that would work well for most users.

**Task:** Implement a font selection system that provides familiar, professional fonts similar to those found in Microsoft Word or Google Docs, with sensible defaults and an appropriate size range.

**Action:**
1. Created a curated list of 13 standard fonts commonly used in professional text editors (Arial, Calibri, Times New Roman, Georgia, Verdana, Helvetica, Tahoma, Trebuchet MS, Comic Sans MS, Courier New, Impact, Palatino, Garamond)
2. Replaced the system font enumeration with our custom font list to avoid overwhelming users
3. Set Arial as the default font with 15pt as the default size for optimal readability
4. Implemented font size range from 15pt to 100pt, removing smaller sizes that are difficult to read
5. Added `setSelectedItem()` calls to ensure dropdown menus display the correct default values on startup
6. Created action listeners for both font type and size dropdowns that immediately apply changes to the text area
7. Set maximum sizes for dropdown menus to maintain toolbar aesthetics

**Result:** Users have access to familiar, professional fonts without being overwhelmed by obscure system fonts. The default of Arial 15pt provides excellent readability from the start, and the UI controls accurately reflect the current font settings. Font changes apply instantly, providing immediate visual feedback.

---

### Challenge 4: Implementing Text Formatting Features

**Situation:** Basic text editors need formatting capabilities to emphasize content. We needed to implement a bold text feature that users could easily access and that would work reliably with different fonts and sizes. Initially, the project scope included both bold and italic, but we needed to simplify.

**Task:** Implement a bold text formatting feature with a toolbar button while removing unnecessary formatting options like italic to keep the interface simple and focused.

**Action:**
1. Created a bold button with an icon and text label in the toolbar
2. Implemented an action listener that toggles the font style between `Font.PLAIN` and `Font.BOLD`
3. Used `deriveFont()` method to change font style while preserving font family and size
4. Added logic to detect current font style and toggle appropriately (bold to plain, plain to bold)
5. Removed italic button functionality to simplify the interface
6. Cleaned up related code including icon imports, button declarations, and event handlers
7. Ensured the bold feature works seamlessly with font type and size changes

**Result:** Users can easily apply bold formatting to text with a single button click. The toggle behavior is intuitive - clicking again removes the bold formatting. The simplified toolbar without italic keeps the interface clean while still providing essential formatting capabilities.

---

### Challenge 5: Adding Search and Replace Functionality

**Situation:** Users needed the ability to quickly find specific text within documents and optionally replace it. This required a streamlined interface that didn't block the user's view of the text, which typical popup dialogs often do.

**Task:** Implement a non-intrusive sidebar-based search and replace feature that allows users to find occurrences, highlight matched text, and replace terms efficiently.

**Action:**
1. Created a specialized `SearchSidebar` class extending `JPanel` that sits to the right of the text area.
2. Implemented real-time text highlighting using `Highlighter.addHighlight` as users type in the search field.
3. Added "Next" buttons with logic to cycle through matched occurrences using regex `Matcher.find()`.
4. Utilized `Pattern.quote()` to ensure searching treats special characters as literals, not regex commands, to prevent errors.
5. Implemented "Replace One" and "Replace All" functionality that updates the text pane content while preserving cursor position where possible.

**Result:** Users have a powerful, always-available search tool that helps them navigate code and text seamlessly. The sidebar approach keeps the main workspace clean while offering advanced features like real-time highlighting.

---

### Challenge 6: Implementing Drag and Drop File Support

**Situation:** Modern applications should support drag and drop functionality for convenience. Users expect to be able to drag files from their file manager directly into the text editor to open them, rather than always using the File menu.

**Task:** Implement drag and drop support that allows users to drop files onto the text area to open them, while ensuring only supported file types are accepted and properly handled.

**Action:**
1. Created a `DropTargetListener` to handle drag and drop events
2. Instantiated a `DropTarget` and attached it to the text area
3. Implemented the `drop()` method to handle file drops
4. Added logic to accept only supported file extensions (.txt, .dat, .log, .xml, .mf, .html, .java, .c, .cpp)
5. Extracted file paths from the transfer data using `DataFlavor.javaFileListFlavor`
6. Validated file extensions against the supported list before opening
7. Reused existing file reading logic with `FileInputStream` to load dropped files
8. Updated the window title to reflect the opened file name
9. Cleared the text area before loading new content to avoid mixing files
10. Enhanced to support universal file types through a centralized extension list.

**Result:** Users can now simply drag files from their file manager and drop them into the editor to open them instantly. The feature checks file types and only accepts supported formats, preventing errors. This provides a modern, intuitive alternative to the traditional File > Open workflow.

---

### Challenge 7: Adding Document Statistics and Real-time Updates

**Situation:** Users working with documents often need to know the length, number of lines, and word count of their text. This information is crucial for meeting document requirements or tracking writing progress.

**Task:** Implement real-time document statistics that display character count, line count, and word count in the window title, updating automatically as users type.

**Action:**
1. Added a `KeyListener` to the text area to detect typing events
2. Implemented the `keyReleased()` event handler to update statistics after each keystroke
3. Used `getText().length()` to calculate total character count
4. Implemented line counting by splitting text on newline characters and counting array elements
5. Calculated word count by splitting text on whitespace using regex `\\s+` and counting tokens
6. Updated the window title format to display: "Filename | AppName [ Length: X Lines: Y Words: Z ]"
7. Ensured statistics update in real-time without impacting typing performance
8. Added an edit flag to track when content has been modified for save prompts

**Result:** Users can see document statistics at a glance in the window title without interrupting their work. The real-time updates provide instant feedback about document length and structure. This feature helps users meet document requirements and track their writing progress efficiently.

---

### Challenge 8: Implementing a Consistent "Midnight" Dark Theme

**Situation:** We wanted to establish a unique visual identity for our editor that would reduce eye strain, but standard Java Swing components are notoriously difficult to style consistently. We needed a professional "Midnight" dark theme without the jarring, disjointed look of default UI elements.

**Task:** Develop a centralized theme engine to enforce a consistent dark palette across all UI components, including the editor, menu bars, buttons, and scrollbars, and ensure icons matched the theme.

**Action:**
1. Created a comprehensive `MidnightTheme.java` helper class to centralize all color definitions (Deep Midnight Background, Component BG, Foreground White).
2. Implemented a design system with reusable styling methods (e.g., `styleButton`, `styleMenuBar`) that could be applied to any component.
3. Enforced transparency (`setOpaque(false)`) on toolbar buttons to allow the dark theme background to show through seamlessly.
4. Implemented a custom `ScrollBarUI` to completely override the operating system's default light scrollbars with dark, theme-aware tracking and thumb controls.

**Result:** A stunning, professional-grade dark interface that feels modern and unified. The application has a custom, premium aesthetic that distinguishes it from standard Java applications.

---

### Challenge 9: Solving Icon Transparency Artifacts with Luma Masking

**Situation:** We chose to use a set of standard icons for our toolbar, but these icons came with white backgrounds that looked like ugly white squares against our new dark theme. We needed a way to use these assets without manually editing every single image file or finding a new icon set.

**Task:** Implement an advanced image processing solution to dynamically adapt existing icons to the dark theme at runtime.

**Action:**
1. Developed a custom `RGBImageFilter` in `MidnightTheme.java` that analyzes pixel brightness (Luma).
2. Implemented a "Luma-to-Alpha" conversion algorithm: pixels that are white (high brightness) become transparent, while dark pixels (content) are transformed to the theme's foreground color (Light Gray).
3. Applied this filter dynamically to all `ImageIcon` instances during initialization, effectively "inverting" the icons for dark mode while preserving their shape and antialiasing.

**Result:** The toolbar icons appear as clean, floating vectors that perfectly match the theme, without any jagged white edges or blocks, demonstrating a sophisticated approach to asset management.

---

### Challenge 10: Enabling Rich Syntax Highlighting for Code

**Situation:** We needed our editor to be more than just a notepad; it needed to support programming tasks. This required independent coloring for keywords, strings, and comments, which the standard `JTextArea` component does not support (it allows only one font/color for the whole text).

**Task:** Implement a rich text editing environment capable of real-time, regex-based syntax highlighting.

**Action:**
1. Selected `JTextPane` as the core editor component for its support of `StyledDocument` and rich text attributes.
2. Created a `HighlightText` class that defines specific `SimpleAttributeSet` styles for different token types (Keywords: Cyan, Strings: Orange, Comments: Gray).
3. Utilized Java's regex `Matcher` to identify syntax patterns in real-time.
4. Implemented a `performHighlighting()` method that iterates through matches and blindly applies character attributes without altering the text content.
5. Optimized performance by triggering highlighting updates on a slight delay (debounce) to prevent lag while typing.

**Result:** Code files are now far easier to read and edit. Java keywords stand out in cyan, strings in orange, and comments are distinct. This transforms the tool from a simple notepad into a capable code editor.

---

### Challenge 11: Scaling to Universal Syntax Highlighting and File Support

**Situation:** We expected our users to work with a wide variety of languages (Web, Data, Scripting), not just C or Java. Hardcoding support for each new language would be unmaintainable and repetitive.

**Task:** Create a scalable, data-driven architecture to support any number of languages with minimal code changes.

**Action:**
1. Designed a centralized `SupportedKeywords` dictionary to act as the single source of truth for language definitions.
2. Implemented a generalized `getKeywordsFor(extension)` method that dynamically fetches the correct keyword set based on file extension.
3. Built the `UI` file chooser and drag-and-drop filters to accept a dynamic "All Supported Code Files" list instead of hardcoded extensions.
4. Enhanced `HighlightText.java` with a "Unified Regex" strategy that detects various comment styles simultaneously (e.g., `//`, `#`, `<!-- -->`, `--`), ensuring comments are correctly colored regardless of the language.

**Result:** The application supports **12+ languages** out of the box with full syntax highlighting. Adding a new language is now as simple as adding a keyword list to the dictionary, making the system highly extensible and future-proof.
