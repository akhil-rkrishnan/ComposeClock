# Compose Clock

Compose Clock is a user-friendly analog clock application crafted with Jetpack Compose, Google's innovative UI toolkit for developing native Android applications. It serves as an illustrative guide on integrating custom user interfaces through the utilization of paths and arcs within the Compose Canvas framework.

### About

The Compose Clock app ingeniously displays time in the 12-hour format, showcasing hours ranging from 1 to 12 on its analog interface. Leveraging foundational trigonometric principles, the clock meticulously renders its components, including the clock face and hands. This approach not only delivers a visually appealing user experience but also provides a valuable learning opportunity for developers interested in mastering custom UI implementation with Jetpack Compose.

### Architecture

Embracing the MVVM (Model-View-ViewModel) architecture, Compose Clock ensures a robust and maintainable codebase. This architectural pattern promotes separation of concerns by dividing the application into three distinct layers:

Model: Represents the data and business logic of the application. In Compose Clock, the model layer manages time-related data and operations, facilitating seamless interaction between the view and view model layers.

View: Corresponds to the user interface components responsible for presenting data to the user. In this app, the view layer encompasses the visual representation of the analog clock, providing users with an intuitive means of interpreting time.

ViewModel: Acts as an intermediary between the model and view layers, orchestrating data flow and business logic. By adopting the MVVM architecture, Compose Clock promotes code reusability, testability, and maintainability, thereby enhancing the overall development experience.
