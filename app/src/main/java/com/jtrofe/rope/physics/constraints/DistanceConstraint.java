package com.jtrofe.rope.physics.constraints;

import com.jtrofe.rope.physics.Vec;
import com.jtrofe.rope.physics.gameobjects.GameObject;

/**
 * Created by jtrofe200 on 2/4/2016
 */
public class DistanceConstraint extends Constraint {

    protected float mDistance;

    public DistanceConstraint(GameObject bodyA, GameObject bodyB, float distance){
        super(bodyA, bodyB);

        this.mDistance = distance;
    }

    @Override
    public void Solve(float timeStep){
        final Vec axis = mBodyB.GetPosition().Subtract(mBodyA.GetPosition());
        final Vec unitAxis = axis.Normalize();
        final float currentDistance = axis.Length();

        final float relVel = mBodyB.GetLinearVelocity().Subtract(mBodyA.GetLinearVelocity()).Dot(unitAxis);

        float relDist = currentDistance - mDistance;

        float remove = relVel + relDist / timeStep;
        float impulse = remove / (mBodyA.GetInvMass() + mBodyB.GetInvMass());

        Vec I = unitAxis.ScalarMultiply(impulse);

        mBodyA.ApplyImpulse(I, mBodyA.GetPosition());
        mBodyB.ApplyImpulse(I.Negate(), mBodyB.GetPosition());
    }
}
