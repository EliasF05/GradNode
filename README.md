# GradNode: Basic AutoGrad Engine in Java (Work in Progress)

## Description
Use GradNode, GradNodeVec and GradNodeMat objects to perform arithmetic operations while Gradients are being kept track of.

## Usage
Use GradeNodeMat/GradeNodeVec objects to build Deep-Learning Models, and adjust their values based on the Parameters.<br>
To Get the Gradient of the value you want to take the derivative of call value.backward(), and the Gradient with respect to every other GradeNode object will be stored in those Objects.
