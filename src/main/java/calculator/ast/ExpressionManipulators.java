package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;

import java.lang.Math;

/**
 * All of the public static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Checks to make sure that the given node is an operation AstNode with the expected
     * name and number of children. Throws an EvaluationError otherwise.
     */
    private static void assertNodeMatches(AstNode node, String expectedName, int expectedNumChildren) {
        if (!node.isOperation()
                && !node.getName().equals(expectedName)
                && node.getChildren().size() != expectedNumChildren) {
            throw new EvaluationError("Node is not valid " + expectedName + " node.");
        }
    }

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     * 
     * This method is required to handle the following binary operations
     *      +, -, *, /, ^
     *  (addition, subtraction, multiplication, division, and exponentiation, respectively) 
     * and the following unary operations
     *      negate, sin, cos
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(Environment env, AstNode node) {
        assertNodeMatches(node, "toDouble", 1);
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(env.getVariables(), exprToConvert));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            if (!variables.containsKey(node.getName())) {
                throw new EvaluationError("Variable is undefined.");
            }
            return toDoubleHelper(variables, variables.get(node.getName()));
        } else {
            String name = node.getName();
            IList<AstNode> children = node.getChildren();
            if (name.equals("+")) {
                return toDoubleHelper(variables, children.get(0))
                        + toDoubleHelper(variables, children.get(1));
            } else if (name.equals("-")) {
                return toDoubleHelper(variables, children.get(0))
                        - toDoubleHelper(variables, children.get(1));
            } else if (name.equals("*")) {
                return toDoubleHelper(variables, children.get(0))
                        * toDoubleHelper(variables, children.get(1));
            } else if (name.equals("/")) {
                return toDoubleHelper(variables, children.get(0))
                        / toDoubleHelper(variables, children.get(1));
            } else if (name.equals("^")) {
                return Math.pow(toDoubleHelper(variables, children.get(0)),
                        toDoubleHelper(variables, children.get(1)));
            } else if (name.equals("negate")) {
                return -1 * toDoubleHelper(variables, children.get(0));
            } else if (name.equals("sin")) {
                return Math.sin(toDoubleHelper(variables, children.get(0)));
            } else if (name.equals("cos")) {
                return Math.cos(toDoubleHelper(variables, children.get(0)));
            } else { // operation is undefined
                throw new EvaluationError("Operation is undefined.");
            }
        }
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        assertNodeMatches(node, "simplify", 1);
        AstNode expToConvert = node.getChildren().get(0);
        return simplifyHelper(env.getVariables(), expToConvert);
    }

    private static AstNode simplifyHelper(IDictionary<String, AstNode> variables, AstNode node) {
        if (node.isVariable()) {
            if (variables.containsKey(node.getName())) {
                node = variables.get(node.getName());
                if (node.isOperation()) {
                    node = expandVariable(node);
                }
            }
        } 
        if (node.isOperation()) {
            node = simplifyExpr(variables, node);
        }
        return node;
    }

    private static AstNode expandVariable(AstNode node) {
        AstNode expanded = new AstNode(node.getName(), new DoubleLinkedList<AstNode>());
        for (AstNode child : node.getChildren()) {
            expanded.getChildren().add(child);
        }
        return expanded;
    }

    private static AstNode simplifyExpr(IDictionary<String, AstNode> variables, AstNode node) {
        IList<AstNode> children = node.getChildren();
        AstNode leftNode = children.get(0);
        if (leftNode.isOperation() || leftNode.isVariable()) {
            children.set(0, simplifyHelper(variables, leftNode));
        }
        if (children.size() > 1) {
            AstNode rightNode = children.get(1);
            if (rightNode.isOperation() || rightNode.isVariable()) {
                children.set(1, simplifyHelper(variables, rightNode));                   
            }
            if (leftNode.isNumber() && rightNode.isNumber()) {
                if (node.getName().equals("+") || node.getName().equals("-") ||
                node.getName().equals("*")) {
                    node = new AstNode(toDoubleHelper(variables, node));
                }
            }
        }
        return node;
    }

    /**
     * Accepts an Environment variable and a 'plot(exprToPlot, var, varMin, varMax, step)'
     * AstNode and generates the corresponding plot on the ImageDrawer attached to the
     * environment. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {
        assertNodeMatches(node, "plot", 5);
        IList<AstNode> children = node.getChildren();
        AstNode var = children.get(1);
        double varMin = toDoubleHelper(env.getVariables(), children.get(2));
        double varMax = toDoubleHelper(env.getVariables(), children.get(3));
        double step = toDoubleHelper(env.getVariables(), children.get(4));
        if (varMin > varMax) {
            throw new EvaluationError("varMin must be less than varMax");
        }
        if (env.getVariables().containsKey(var.getName())) {
            throw new EvaluationError("Given variable is already defined");
        }
        if (step < 0) {
            throw new EvaluationError("Please give a positive, non-zero step value.");
        }
        handlePlot(env, children.get(0), var, varMin, varMax, step);
        return new AstNode(1);
    }

    private static void handlePlot(Environment env, AstNode expr, AstNode var, 
                                   double min, double max, double step) {
        IDictionary<String, AstNode> variables = env.getVariables();                               
        IList<Double> xValues = new DoubleLinkedList<Double>();
        IList<Double> yValues = new DoubleLinkedList<Double>();
        int numValues = (int) ((max - min) / step) + 1;
        for (int i = 0; i < numValues; i++) {
            variables.put(var.getName(), new AstNode(min));
            xValues.add(min);
            yValues.add(toDoubleHelper(variables, expr));
            min += step;
        }
        env.getImageDrawer().drawScatterPlot("Plot", var.getName(), "output", xValues, yValues);
        variables.remove(var.getName());
    }

}
